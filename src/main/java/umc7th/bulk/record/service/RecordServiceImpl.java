package umc7th.bulk.record.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;
import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.entity.Record;
import umc7th.bulk.record.gpt.service.AiCallService;
import umc7th.bulk.record.repository.RecordRepository;
import umc7th.bulk.record.upload.S3Service;
import umc7th.bulk.recordedFood.entity.RecordedFood;
import umc7th.bulk.recordedFood.repository.RecordedFoodRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordedFoodRepository recordedFoodRepository;
    private final MealMealItemMappingRepository mealMealItemMappingRepository;
    private final S3Service s3Service;
    private final AiCallService aiCallService;
    private final UserService userService;

    @Transactional
    public RecordResponseDto createRecord(RecordRequestDto.Create requestDto) {
        // 사용자 조회
        User user = userService.getAuthenticatedUserInfo();

        // MealType 변환
        MealType type = requestDto.getMealType();

        // 기존 기록이 있는지 확인
        Record existingRecord = recordRepository.findByUserAndDateAndMealType(user, requestDto.getDate(), type)
                .orElse(null);

        if (existingRecord != null) {
            throw new IllegalArgumentException("이미 해당 날짜와 끼니에 대한 기록이 존재합니다.");
        }

        // 사용자의 끼니(MealType)에 해당하는 식단 데이터 조회
        List<MealMealItemMapping> mealMappings = mealMealItemMappingRepository.findByMeal_LocalDateAndMeal_Type(
                requestDto.getDate(), requestDto.getMealType());

        if (mealMappings.isEmpty()) {
            throw new IllegalArgumentException("해당 끼니의 식단을 찾을 수 없습니다.");
        }

        // 새로운 Record 생성
        Record record = Record.builder()
                .user(user)
                .date(requestDto.getDate())
                .mealType(type)
                .ateOnPlan(true)
                .totalCalories(0L) // 기본값 설정
                .totalCarbs(0L)
                .totalProtein(0L)
                .totalFat(0L)
                .build();

        Record savedRecord = recordRepository.save(record);

        // MealItem을 기반으로 RecordedFood 생성
        List<RecordedFood> recordedFoods = mealMappings.stream()
                .map(MealMealItemMapping::getMealItem)
                .distinct()
                .map(mealItem -> RecordedFood.builder()
                        .record(savedRecord)
                        .foodId(mealItem)
                        .quantity(mealItem.getGram() != null ? mealItem.getGram().intValue() : 100)
                        .calories(mealItem.getCalories())
                        .carbos(mealItem.getCarbos())
                        .proteins(mealItem.getProteins())
                        .fats(mealItem.getFats())
                        .build())
                .collect(Collectors.toList());

        recordedFoodRepository.saveAll(recordedFoods);

        // 기록한 식품의 count 증가
        recordedFoods.forEach(
                food -> {
                    MealItem mealItem = food.getFoodId();
                    mealItem.increaseRecordFoodCount();
                }
        );

        // 영양소 합산
        Long totalCalories = recordedFoods.stream().mapToLong(RecordedFood::getCalories).sum();
        Long totalCarbs = recordedFoods.stream().mapToLong(RecordedFood::getCarbos).sum();
        Long totalProtein = recordedFoods.stream().mapToLong(RecordedFood::getProteins).sum();
        Long totalFat = recordedFoods.stream().mapToLong(RecordedFood::getFats).sum();

        // Record에 영양소 값 업데이트
        savedRecord.updateNutrients(totalCalories, totalCarbs, totalProtein, totalFat);
        recordRepository.save(savedRecord);


        // Response 생성
        return RecordResponseDto.builder()
                .recordId(savedRecord.getId())
                .ateOnPlan(savedRecord.isAteOnPlan())
                .mealType(savedRecord.getMealType())
                .date(savedRecord.getDate())
                .totalCalories(totalCalories)
                .totalCarbs(totalCarbs)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .foodPhoto(null) // 식단 기반 저장이므로 사진 없음
                .gptAnalysis(null) // GPT 응답 없음
                .foods(recordedFoods.stream()
                        .map(food -> RecordResponseDto.FoodResponse.builder()
                                .foodName(food.getFoodId().getName())
                                .quantity(food.getQuantity())
                                .grade(food.getFoodId().getGrade())
                                .gradePeopleNum(food.getFoodId().getGradePeopleNum())
                                .carbos(food.getCarbos())
                                .proteins(food.getProteins())
                                .fats(food.getFats())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }



    @Transactional
    public RecordResponseDto createNotFollowedRecord(RecordRequestDto.CreateNotFollowed requestDto) {
        // 사용자 조회
        User user = userService.getAuthenticatedUserInfo();

        log.info("🚀 createNotFollowedRecord 요청 시작: userId={}, date={}, mealType={}", user.getId(), requestDto.getDate(), requestDto.getMealType());

        // MealType 변환
        MealType type = requestDto.getMealType();

        String uploadedImageUrl = null;
        String gptRawResponseString = null;

        // 이미지 업로드 및 GPT 분석 처리
        if (requestDto.getImage() != null) {
            log.info("📸 이미지 업로드 시작...");
            uploadedImageUrl = s3Service.uploadFile("record", requestDto.getImage());
            log.info("✅ 이미지 업로드 완료: {}", uploadedImageUrl);

            // GPT API 요청
            String requestText = "이 이미지는 음식 사진입니다.  \n" +
                    "해당 음식의 영양 성분을 분석하고 아래 예시와 동일한 JSON 형식만 반환하세요.  \n" +
                    "**추가적인 설명, 텍스트, 코드 블록(\\\\\\json 등)을 포함하지 마세요.**  \n" +
                    "반드시 JSON 데이터만 출력하세요.  \n\n" +
                    "예시:\n" +
                    "{\n" +
                    "  \"date\": \"YYYY-MM-DD\",\n" +
                    "  \"meal_type\": \"점심\",\n" +
                    "  \"total_calories\": 705,\n" +
                    "  \"macros\": {\n" +
                    "    \"carbohydrates\": 71,\n" +
                    "    \"protein\": 39,\n" +
                    "    \"fat\": 29\n" +
                    "  },\n" +
                    "  \"foods\": [\n" +
                    "    {\n" +
                    "      \"name\": \"현미밥\",\n" +
                    "      \"amount\": \"200g\",\n" +
                    "      \"calories\": 280,\n" +
                    "      \"carbohydrates\": 35,\n" +
                    "      \"protein\": 6,\n" +
                    "      \"fat\": 2\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"name\": \"닭가슴살\",\n" +
                    "      \"amount\": \"100g\",\n" +
                    "      \"calories\": 165,\n" +
                    "      \"carbohydrates\": 0,\n" +
                    "      \"protein\": 31,\n" +
                    "      \"fat\": 3.6\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n\n" +
                    "**출력 형식:**  \n" +
                    "- 반드시 JSON 데이터만 반환하세요.  \n" +
                    "- 설명이나 텍스트를 절대 추가하지 마세요. 강조합니다.  \n" +
                    "- 코드 블록(\\\\\\json 등) 없이 JSON만 제공하세요.";
            try {
                log.info("🧠 GPT API 요청 시작...");
                gptRawResponseString = aiCallService.requestImageAnalysis(requestDto.getImage(), requestText)
                        .getChoices().get(0).getMessage().getContent();
                log.info("✅ GPT API 응답 성공 (Raw Response):\n{}", gptRawResponseString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // GPT 응답을 JSON으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Long totalCalories = 0L;
        Long totalCarbs = 0L;
        Long totalProtein = 0L;
        Long totalFat = 0L;
        List<RecordResponseDto.FoodResponse> foods = new ArrayList<>();

        try {
            JsonNode gptResponseJson = objectMapper.readTree(gptRawResponseString);
            totalCalories = gptResponseJson.get("total_calories").asLong();
            JsonNode macros = gptResponseJson.get("macros");
            totalCarbs = macros.get("carbohydrates").asLong();
            totalProtein = macros.get("protein").asLong();
            totalFat = macros.get("fat").asLong();

            for (JsonNode foodNode : gptResponseJson.get("foods")) {
                foods.add(RecordResponseDto.FoodResponse.builder()
                        .foodName(foodNode.get("name").asText())
                        .quantity(100) // 기본값 설정
                        .carbos(foodNode.get("carbohydrates").asLong(0)) // 기본값 0 처리
                        .proteins(foodNode.get("protein").asLong(0))
                        .fats(foodNode.get("fat").asLong(0))
                        .build());
            }

        } catch (JsonProcessingException e) {
            log.error("❌ GPT 응답 JSON 파싱 오류", e);
            throw new RuntimeException("GPT 응답을 파싱하는 동안 오류 발생");
        }

        // Record 생성
        Record record = Record.builder()
                .user(user)
                .date(requestDto.getDate())
                .mealType(type)
                .foodPhoto(uploadedImageUrl)
                .ateOnPlan(requestDto.getImage() == null)
                .totalCalories(totalCalories)
                .totalCarbs(totalCarbs)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .build();

        Record savedRecord = recordRepository.save(record);
        log.info("✅ Record 저장 완료: recordId={}", savedRecord.getId());

        // Response 생성
        return RecordResponseDto.builder()
                .recordId(savedRecord.getId())
                .ateOnPlan(savedRecord.isAteOnPlan())
                .mealType(savedRecord.getMealType())
                .date(savedRecord.getDate())
                .foodPhoto(uploadedImageUrl)
                .gptAnalysis(gptRawResponseString)
                .totalCalories(totalCalories)
                .totalCarbs(totalCarbs)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .foods(foods)
                .build();
    }



    @Transactional(readOnly = true)
    public RecordResponseDto getRecord(User user, LocalDate date, String mealType) {

        // MealType 변환
        MealType type = MealType.valueOf(mealType.toUpperCase());

        // Lazy Loading 문제 해결을 위해 fetch join 사용
        Record record = recordRepository.findByUserAndDateAndMealTypeWithFoods(user, date, type)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));

        // RecordedFood 목록 조회
        List<RecordedFood> recordedFoods = record.getFoods();

        // ✅ Lazy Loading 문제로 인해 recordedFoods가 빈 배열이 아닐지 체크
        if (recordedFoods.isEmpty()) {
            throw new IllegalStateException("기록된 음식이 없습니다. 데이터베이스를 확인하세요.");
        }

        // 응답으로 반환할 음식 목록
        List<RecordResponseDto.FoodResponse> foods = recordedFoods.stream()
                .map(food -> RecordResponseDto.FoodResponse.builder()
                        .foodName(food.getFoodId().getName())
                        .quantity(food.getQuantity())
                        .grade(food.getFoodId().getGrade())
                        .gradePeopleNum(food.getFoodId().getGradePeopleNum())
                        .carbos(food.getFoodId().getCarbos())
                        .proteins(food.getFoodId().getProteins())
                        .fats(food.getFoodId().getFats())
                        .build())
                .collect(Collectors.toList());

        // ✅ DB에서 제대로 값이 저장된 것이 맞다면 그대로 반환해야 함
        return RecordResponseDto.builder()
                .recordId(record.getId())
                .ateOnPlan(record.isAteOnPlan())
                .mealType(record.getMealType())
                .date(record.getDate())
                .totalCalories(record.getTotalCalories()) // ✅ totalCalories가 0이면 DB 확인 필요
                .totalCarbs(record.getTotalCarbs())
                .totalProtein(record.getTotalProtein())
                .totalFat(record.getTotalFat())
                .foods(foods)
                .build();
    }

    @Transactional(readOnly = true)
    public RecordResponseDto.TodaySummary getTodayRecord(User user) {
        LocalDate today = LocalDate.now();
        List<Record> todayRecords = recordRepository.findByUserAndDate(user, today);

        // 기록이 없을 경우 기본값 반환
        if (todayRecords.isEmpty()) {
            return RecordResponseDto.TodaySummary.builder()
                    .date(today)
                    .totalCalories(0L)
                    .totalCarbs(0L)
                    .totalProtein(0L)
                    .totalFat(0L)
                    .build();
        }

        // 영양소 합산
        Long totalCalories = todayRecords.stream().mapToLong(Record::getTotalCalories).sum();
        Long totalCarbs = todayRecords.stream().mapToLong(Record::getTotalCarbs).sum();
        Long totalProtein = todayRecords.stream().mapToLong(Record::getTotalProtein).sum();
        Long totalFat = todayRecords.stream().mapToLong(Record::getTotalFat).sum();

        return RecordResponseDto.TodaySummary.builder()
                .date(today)
                .totalCalories(totalCalories)
                .totalCarbs(totalCarbs)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .build();
    }


}