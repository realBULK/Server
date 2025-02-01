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
import umc7th.bulk.meal.dto.MealDTO;
import umc7th.bulk.meal.dto.MealResponseDTO;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.meal.exception.MealErrorCode;
import umc7th.bulk.meal.exception.MealErrorException;
import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealItem.entity.MealItem;
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
    public MealResponseDTO.MealPreviewDTO getMealItems(Long dailyMealId, MealType type, Long cursorId, int pagSize) {

        // 하루 식단 확인
        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new DailyMealException(DailyMealErrorCode.NOT_FOUND));
        
        // 끼니 조회
        Meal meal = dailyMeal.getMeals().stream()
                .filter(m -> m.getType() == type)
                .findFirst()
                .orElseThrow(() -> new MealErrorException(MealErrorCode.NOT_FOUND));

        // 유저 조회
        Long userId = meal.getDailyMeal().getMealPlan().getUser().getId();

        // MealMealItemMapping 통해 MealItem 가져오기
        List<MealMealItemMapping> mealItemMappings = mappingRepository.findByMealId(meal.getId());
        List<MealItem> mealItems = mealItemMappings.stream()
                .map(mealMealItemMapping -> mealMealItemMapping.getMealItem())
                .toList();
        
        // 칼로리, 탄단지 계산
        Long mealCalories = mealItems.stream().mapToLong(MealItem::getCalories).sum();
        Long mealCarbos = mealItems.stream().mapToLong(MealItem::getCarbos).sum();
        Long mealProteins = mealItems.stream().mapToLong(MealItem::getProteins).sum();
        Long mealFats = mealItems.stream().mapToLong(MealItem::getFats).sum();

        // Meal 축소본
        LocalDate date = meal.getDailyMeal().getDate();
        MealDTO.MealSummaryDTO summaryDTO = new MealDTO.MealSummaryDTO(date, type, mealCalories, mealCarbos, mealProteins, mealFats);

        // Meal의 음식 목록 - 페이지네이션 필요
        Pageable pageable = PageRequest.of(0, pagSize);
        Slice<MealMealItemMapping> mappingSlice = mappingRepository.findMealItemsByUserAndMealWithCursor(userId, type, cursorId, pageable);

        List<MealItemDTO.MealItemPreviewDTO> items = mappingSlice.getContent().stream()
                .map(mapping -> new MealItemDTO.MealItemPreviewDTO(mapping.getMealItem()))
                .toList();

        Long nextCursorId = (mappingSlice.hasNext() && !items.isEmpty()) ? items.get(items.size() - 1).getId() : null;

        return MealResponseDTO.MealPreviewDTO.from(summaryDTO, items, nextCursorId);
    }
}
