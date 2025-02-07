package umc7th.bulk.mealMealItemMapping.service.command;

import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

public interface MealMealItemMappingCommandService {

    void deleteMealMealItemMapping(Long mappingId);

    MealMealItemMapping createMapping(Meal meal, MealItem mealItem);

}
