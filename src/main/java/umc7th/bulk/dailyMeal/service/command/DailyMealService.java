package umc7th.bulk.dailyMeal.service.command;

import umc7th.bulk.dailyMeal.dto.DailyMealResponseDTO;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;

public interface DailyMealService {

    DailyMeal createDailyMeal(MealPlan mealPlan, MealPlanRequestDTO.DailyMealDTO dto);

    DailyMealResponseDTO.DailyMealGetResponseDTO getDailyMeal(Long userId, Long dailyMealId);
}
