package umc7th.bulk.mealItem.dto;

import lombok.Getter;
import umc7th.bulk.mealItem.entity.MealItem;

public class MealItemDTO {

    @Getter
    public static class MealItemInfoDTO {

        private String name;

        public MealItemInfoDTO(MealItem mealItem) {
            name = mealItem.getName();
        }
    }
}
