package umc7th.bulk.record.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;
import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.dto.RecordWithGPTResponseDTO;
import umc7th.bulk.record.entity.Record;
import umc7th.bulk.record.gpt.service.AiCallService;
import umc7th.bulk.record.repository.RecordRepository;
import umc7th.bulk.record.upload.S3Service;
import umc7th.bulk.recordedFood.entity.RecordedFood;
import umc7th.bulk.recordedFood.repository.RecordedFoodRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordedFoodRepository recordedFoodRepository;
    private final UserRepository userRepository;
    private final MealMealItemMappingRepository mealMealItemMappingRepository;
    private final S3Service s3Service;
    private final AiCallService aiCallService;
    private final MealItemRepository mealItemRepository;

    @Transactional
    public RecordResponseDto createRecord(RecordRequestDto.Create requestDto) {
        // 사용자 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

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
                .build();

        Record savedRecord = recordRepository.save(record);

        // MealItem을 기반으로 RecordedFood 생성
        List<RecordedFood> recordedFoods = mealMappings.stream()
                .map(MealMealItemMapping::getMealItem) // MealItem 객체 리스트로 변환
                .distinct() // MealItem 기준 중복 제거
                .map(mealItem -> RecordedFood.builder()
                        .record(savedRecord)
                        .foodId(mealItem)
                        .quantity(mealItem.getGram() != null ? mealItem.getGram().intValue() : 100) // 기본값 설정
                        .build())
                .collect(Collectors.toList());

        recordedFoodRepository.saveAll(recordedFoods);

        // Response 생성
        return RecordResponseDto.builder()
                .recordId(savedRecord.getId())
                .ateOnPlan(savedRecord.isAteOnPlan())
                .mealType(savedRecord.getMealType())
                .date(savedRecord.getDate())
                .foods(recordedFoods.stream()
                        .map(food -> RecordResponseDto.FoodResponseDto.builder()
                                .foodId(food.getFoodId().getId())
                                .foodName(food.getFoodId().getName())
                                .quantity(food.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public RecordWithGPTResponseDTO createNotFollowedRecord(RecordRequestDto.CreateNotFollowed requestDto) {
        log.info("🚀 createNotFollowedRecord 요청 시작: userId={}, date={}, mealType={}",
                requestDto.getUserId(), requestDto.getDate(), requestDto.getMealType());

        // 사용자 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    log.error("❌ 유저 조회 실패: userId={}", requestDto.getUserId());
                    return new IllegalArgumentException("Invalid user ID");
                });

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
                    "**추가적인 설명, 텍스트, 코드 블록(\\`\\`\\`json 등)을 포함하지 마세요.**  \n" +
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
                    "- 설명이나 텍스트를 절대 추가하지 마세요.  \n" +
                    "- 코드 블록(\\`\\`\\`json 등) 없이 JSON만 제공하세요.";

            try {
                log.info("🧠 GPT API 요청 시작...");
                gptRawResponseString = aiCallService.requestImageAnalysis(requestDto.getImage(), requestText)
                        .getChoices().get(0).getMessage().getContent();

                log.info("✅ GPT API 응답 성공 (Raw Response):\n{}", gptRawResponseString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

            // Record 생성
            Record record = Record.builder()
                    .user(user)
                    .date(requestDto.getDate())
                    .mealType(type)
                    .foodPhoto(uploadedImageUrl)
                    .ateOnPlan(requestDto.getImage() == null)
                    .build();

            Record savedRecord = recordRepository.save(record);
            log.info("✅ Record 저장 완료: recordId={}", savedRecord.getId());

            // Response 생성
            log.info("🚀 createNotFollowedRecord 응답 생성 완료: recordId={}", savedRecord.getId());
            return RecordWithGPTResponseDTO.builder()
                    .recordId(savedRecord.getId())
                    .ateOnPlan(savedRecord.isAteOnPlan())
                    .mealType(savedRecord.getMealType())
                    .date(savedRecord.getDate())
                    .foodPhoto(uploadedImageUrl)
                    .gptAnalysis(gptRawResponseString)
                    .build();

    }


    @Transactional(readOnly = true)
    public RecordResponseDto getRecord(Long userId, LocalDate date, String mealType) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // MealType 변환
        MealType type = MealType.valueOf(mealType.toUpperCase());

        // 사용자의 기존 Record 조회
        Record record = recordRepository.findByUserAndDateAndMealType(user, date, type)
                .orElseThrow(() -> new IllegalArgumentException("기록을 찾을 수 없습니다."));

        // 응답으로 반환
        List<RecordResponseDto.FoodResponseDto> foods = record.getFoods().stream()
                .map(food -> RecordResponseDto.FoodResponseDto.builder()
                        .foodId(food.getFoodId().getId())
                        .foodName(food.getFoodId().getName())
                        .quantity(food.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // Response 생성
        return RecordResponseDto.builder()
                .recordId(record.getId())
                .ateOnPlan(record.isAteOnPlan())
                .mealType(record.getMealType())
                .date(record.getDate())
                .foods(foods)
                .build();
    }



}