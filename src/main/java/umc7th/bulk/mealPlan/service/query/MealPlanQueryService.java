package umc7th.bulk.mealPlan.service.query;

import umc7th.bulk.mealPlan.entity.MealPlan;

public interface MealPlanQueryService {

//    MealPlan getMealPlan(Long userId, Long mealPlanId, int pageSize);
    MealPlan getMealPlan(Long userId, Long mealPlanId);
}
