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

    @Getter
    public static class MealItemPreviewDTO {
        private Long id;
        private String name;
        private String unit;
        private Long gradePeopleNum;
        private Double grade;
        private Long calories;
        private Long carbos;
        private Long proteins;
        private Long fats;
        private Long gram;

        public MealItemPreviewDTO(MealItem mealItem) {
            id = mealItem.getId();
            name = mealItem.getName();
            unit = mealItem.getUnit();
            gradePeopleNum = mealItem.getGradePeopleNum();
            grade = mealItem.getGrade();
            calories = mealItem.getCalories();
            carbos = mealItem.getCarbos();
            proteins = mealItem.getProteins();
            fats = mealItem.getFats();
            gram = mealItem.getGram();
        }
    }

    @Getter
    public static class MealItemPopularityDTO {
        private int rank;
        private String name;
        private String unit;
        private Long gradePeopleNum;
        private Double grade;

        public MealItemPopularityDTO(MealItem mealItem, int rank) {
            this.rank = rank;
            name = mealItem.getName();
            unit = mealItem.getUnit();
            gradePeopleNum = mealItem.getGradePeopleNum();
            grade = mealItem.getGrade();
        }
    }
}
