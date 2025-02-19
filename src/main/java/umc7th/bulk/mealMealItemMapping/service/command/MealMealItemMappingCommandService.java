package umc7th.bulk.mealMealItemMapping.service.command;

import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.user.domain.User;

public interface MealMealItemMappingCommandService {

    void deleteMealMealItemMapping(User user, Long mappingId);

    MealMealItemMapping createMapping(Meal meal, MealItem mealItem);

}
