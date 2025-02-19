package umc7th.bulk.mealPlan.service.query;

import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.user.domain.User;

import java.util.List;

public interface MealPlanQueryService {

//    MealPlan getMealPlan(Long userId, Long mealPlanId, int pageSize);
    MealPlan getMealPlan(Long userId, Long mealPlanId);

    List<Long> getMealPlans(User user);
}
