package umc7th.bulk.mealPlan.service.command;

import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;

public interface MealPlanCommandService {

    MealPlan createMealPlanDTO(MealPlanRequestDTO.MealPlanDTO dto, Long userId);
}
