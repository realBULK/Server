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
    public RecordResponseDto createRecord(Long userId, RecordRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // 사용자의 끼니(MealType)에 해당하는 식단 데이터 조회
        List<MealMealItemMapping> mealMappings = mealMealItemMappingRepository.findMealItemsByUserAndMealWithCursor(
                userId, requestDto.getMealType(), null, null).getContent();

        if (mealMappings.isEmpty()) {
            throw new IllegalArgumentException("해당 끼니의 식단을 찾을 수 없습니다.");
        }

        // Record 생성
        Record record = Record.builder()
                .user(user)
                .date(requestDto.getDate())
                .mealType(requestDto.getMealType())
                .foodPhoto(requestDto.getFoodPhoto())
                .ateOnPlan(true)
                .build();

        Record savedRecord = recordRepository.save(record);

        // MealItem을 기반으로 RecordedFood 생성
        List<RecordedFood> recordedFoods = mealMappings.stream()
                .map(mapping -> RecordedFood.builder()
                        .record(savedRecord)
                        .foodId(mapping.getMealItem())
                        .quantity(mapping.getMealItem().getGram().intValue())
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