package umc7th.bulk.meal.service.command;

import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;

public interface MealCommandService {

    Meal createMeal(DailyMeal dailyMeal, MealPlanRequestDTO.MealDTO dto);
}
