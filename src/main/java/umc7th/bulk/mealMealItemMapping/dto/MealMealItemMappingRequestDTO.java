package umc7th.bulk.mealMealItemMapping.dto;

import lombok.Getter;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

public class MealMealItemMappingRequestDTO {

    @Getter
    public static class CreateMealMealItemMappingDTO {

        public MealMealItemMapping toEntity(Meal meal, MealItem mealItem) {
            return MealMealItemMapping.builder()
                    .meal(meal)
                    .mealItem(mealItem)
                    .build();
        }
    }
}
