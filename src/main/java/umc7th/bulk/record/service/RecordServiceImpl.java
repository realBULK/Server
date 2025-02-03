package umc7th.bulk.record.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;
import umc7th.bulk.record.dto.RecordRequestDto;
import umc7th.bulk.record.dto.RecordResponseDto;
import umc7th.bulk.record.entity.Record;
import umc7th.bulk.record.repository.RecordRepository;
import umc7th.bulk.recordedFood.entity.RecordedFood;
import umc7th.bulk.recordedFood.repository.RecordedFoodRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordedFoodRepository recordedFoodRepository;
    private final UserRepository userRepository;
    private final MealMealItemMappingRepository mealMealItemMappingRepository;

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
                .foodPhoto(requestDto.getFoodPhoto())
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