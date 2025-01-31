package umc7th.bulk.meal.dto;

import lombok.Getter;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.dto.MealItemDTO;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

import java.time.LocalDate;
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

            // meamMealItemMapping에서 item들 뽑고 dto 리스트로
            List<MealItem> mealItemList = meal.getMealMealItemMappings().stream()
                    .map(MealMealItemMapping::getMealItem)
                    .toList();
            
            mealItems = mealItemList.stream()
                    .map(MealItemDTO.MealItemInfoDTO::new)
                    .collect(Collectors.toList());

            mealCalories = mealItemList.stream()
                    .mapToLong(MealItem::getCalories)
                    .sum();
            mealCarbos = mealItemList.stream()
                    .mapToLong(MealItem::getCarbos)
                    .sum();
            mealProteins = mealItemList.stream()
                    .mapToLong(MealItem::getProteins)
                    .sum();
            mealFats = mealItemList.stream()
                    .mapToLong(MealItem::getFats)
                    .sum();
        }
    }

    @Getter
    public static class MealSummaryDTO {
        private LocalDate date;
        private MealType type;
        private Long mealCalories;
        private Long mealCarbos;
        private Long mealProteins;
        private Long mealFats;

        public MealSummaryDTO(LocalDate date, MealType type, Long mealCalories, Long mealCarbos, Long mealProteins, Long mealFats) {
            this.date = date;
            this.type = type;
            this.mealCalories = mealCalories;
            this.mealCarbos = mealCarbos;
            this.mealProteins = mealProteins;
            this.mealFats = mealFats;
        }
    }
}
