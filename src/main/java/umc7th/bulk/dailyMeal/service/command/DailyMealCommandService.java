package umc7th.bulk.dailyMeal.service.command;

import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

public interface DailyMealCommandService {

    DailyMeal createDailyMeal(Long mealPlanId, MealPlanRequestDTO.DailyMealDTO dto);
}
