package umc7th.bulk.meal.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.dailyMeal.exception.DailyMealErrorCode;
import umc7th.bulk.dailyMeal.exception.DailyMealException;
import umc7th.bulk.dailyMeal.repository.DailyMealRepository;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.repository.MealRepository;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.service.command.MealItemCommandService;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.service.command.MealMealItemMappingCommandService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealCommandServiceImpl implements MealCommandService {

    private final MealItemCommandService mealItemCommandService;
    private final MealMealItemMappingCommandService mealMealItemMappingCommandService;

    private final MealRepository mealRepository;
    private final DailyMealRepository dailyMealRepository;

    @Override
    public Meal createMeal(Long dailyMealId, MealPlanRequestDTO.MealDTO dto) {

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(
                () -> new DailyMealException(DailyMealErrorCode.NOT_FOUND));

        Meal meal = mealRepository.save(dto.toMealEntity(dto, dailyMeal));

        List<MealPlanRequestDTO.MealItemDTO> mealItems = dto.getMealItems();

        List<MealMealItemMapping> mappings = dto.getMealItems().stream()
                .map(mealItemDTO -> {
                    // mealItem 저장
                    MealItem mealItem = mealItemCommandService.createMealItem(mealItemDTO);
                    return mealMealItemMappingCommandService.createMapping(meal, mealItem);
                })
                .toList();

        return meal;
    }
}
