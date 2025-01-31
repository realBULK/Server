package umc7th.bulk.meal.service.query;

import umc7th.bulk.meal.dto.MealResponseDTO;
import umc7th.bulk.meal.entity.MealType;

public interface MealQueryService {

//    Meal getMeal(Long dailyMealId, MealType mealType, Long cursorId, int pageSize);
    MealResponseDTO.MealPreviewDTO getMealItems(Long dailyMealId, MealType type, Long cursorId, int pageSize);
}
