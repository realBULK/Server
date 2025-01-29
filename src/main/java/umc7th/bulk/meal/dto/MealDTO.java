package umc7th.bulk.meal.dto;

import lombok.Getter;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.dto.MealItemDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MealDTO {

    @Getter
    public static class MealInfoDTO {
        private MealType type;
        private List<MealItemDTO.MealItemInfoDTO> mealItems;
        private Long mealCalories;
        private Long mealCarbos;
        private Long mealProteins;
        private Long mealFats;

        public MealInfoDTO(Meal meal) {
            type = meal.getType();
            mealItems = meal.getMealItems().stream()
                    .map(mealItem -> new MealItemDTO.MealItemInfoDTO(mealItem))
                    .collect(Collectors.toList());

            mealCalories = meal.getMealItems().stream()
                    .mapToLong(mealItemsCalories -> mealItemsCalories.getCalories()).sum();
            mealCarbos = meal.getMealItems().stream()
                    .mapToLong(mealItemsCarbos -> mealItemsCarbos.getCarbos()).sum();
            mealProteins = meal.getMealItems().stream()
                    .mapToLong(mealItemsProteins -> mealItemsProteins.getProteins()).sum();
            mealFats = meal.getMealItems().stream()
                    .mapToLong(mealItemsFats -> mealItemsFats.getFats()).sum();

        }
    }
}
