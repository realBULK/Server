package umc7th.bulk.meal.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.dailyMeal.exception.DailyMealErrorCode;
import umc7th.bulk.dailyMeal.exception.DailyMealException;
import umc7th.bulk.dailyMeal.repository.DailyMealRepository;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.meal.dto.MealDTO;
import umc7th.bulk.meal.dto.MealResponseDTO;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.meal.exception.MealErrorCode;
import umc7th.bulk.meal.exception.MealErrorException;
import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.repository.MealMealItemMappingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealQueryServiceImpl implements MealQueryService{

    private final DailyMealRepository dailyMealRepository;
    private final MealMealItemMappingRepository mappingRepository;
    @Override
    public MealResponseDTO.MealPreviewDTO getMealItems(Long userId, Long dailyMealId, MealType type, Long cursorId, int pagSize) {

        // 하루 식단 확인
        DailyMeal dailyMeal = dailyMealRepository.findByIdWithMealPlanAndUser(dailyMealId).orElseThrow(
                () -> new DailyMealException(DailyMealErrorCode.NOT_FOUND));

        if (!dailyMeal.getMealPlan().getUser().getId().equals(userId)) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }
        
        // 끼니 조회
        Meal meal = dailyMeal.getMeals().stream()
                .filter(m -> m.getType() == type)
                .findFirst()
                .orElseThrow(() -> new MealErrorException(MealErrorCode.NOT_FOUND));


        // 칼로리, 탄단지 계산
        Long mealCalories = meal.getMealCalories();
        Long mealCarbos = meal.getMealCarbos();
        Long mealProteins = meal.getMealProteins();
        Long mealFats = meal.getMealFats();

        // Meal 축소본
        LocalDate date = meal.getDailyMeal().getDate();
        MealDTO.MealSummaryDTO summaryDTO = new MealDTO.MealSummaryDTO(date, type, mealCalories, mealCarbos, mealProteins, mealFats);

        // Meal의 음식 목록 - 페이지네이션 필요
        Pageable pageable = PageRequest.of(0, pagSize);
        Slice<MealMealItemMapping> mappingSlice = mappingRepository.findMealItemsByDailyMealAndMealTypeWithCursor(dailyMealId, type, cursorId, pageable);

        List<MealItemDTO.MealItemPreviewDTO> items = mappingSlice.getContent().stream()
                .map(mapping -> new MealItemDTO.MealItemPreviewDTO(mapping.getMealItem()))
                .toList();

        Long nextCursorId = (mappingSlice.hasNext() && !items.isEmpty()) ? items.get(items.size() - 1).getId() : null;

        return MealResponseDTO.MealPreviewDTO.from(summaryDTO, items, nextCursorId);
    }
}
