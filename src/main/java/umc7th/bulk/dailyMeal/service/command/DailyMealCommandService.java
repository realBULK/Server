package umc7th.bulk.dailyMeal.service.command;

import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;

public interface DailyMealCommandService {

    DailyMeal createDailyMeal(MealPlan mealPlan, MealPlanRequestDTO.DailyMealDTO dto);
}
