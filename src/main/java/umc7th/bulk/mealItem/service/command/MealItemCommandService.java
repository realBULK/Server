package umc7th.bulk.mealItem.service.command;

import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

public interface MealItemCommandService {

    MealItem createMealItem(MealPlanRequestDTO.MealItemDTO dto);
}
