package umc7th.bulk.meal.service.command;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.dailyMeal.repository.DailyMealRepository;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.repository.MealRepository;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealMealItemMapping.service.command.MealMealItemMappingCommandService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealCommandServiceImpl implements MealCommandService {

    private final MealMealItemMappingCommandService mealMealItemMappingCommandService;
    private final MealRepository mealRepository;
    private final DailyMealRepository dailyMealRepository;

    @Override
    @Transactional
    public Meal createMeal(DailyMeal dailyMeal, MealPlanRequestDTO.MealDTO dto) {

        Meal meal = mealRepository.save(dto.toMealEntity(dto, dailyMeal));

        List<MealPlanRequestDTO.MealItemDTO> mealItems = dto.getMealItems();

        List<MealMealItemMapping> mappings = mealItems.stream()
                .map(mealItemDTO -> {
                    // mealItem 저장
//                    MealItem mealItem = mealItemCommandService.createMealItem(mealItemDTO);
                    MealItem mealItemEntity = mealItemDTO.toMealItemEntity(mealItemDTO);
                    return mealMealItemMappingCommandService.createMapping(meal, mealItemEntity);
                })
                .toList();

        return meal;
    }
}