package umc7th.bulk.mealPlan.dto;

import lombok.Getter;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;
import java.util.List;

public class MealPlanRequestDTO {

    @Getter
    public static class TargetNutritionDTO {
        private int BMR;
        private int TDEE;
        private int target_calories;
        private int carbs_target;
        private int protein_target;
        private int fat_target;
    }

    @Getter
    public static class MealItemDTO {
        private String name;
        private Long calories;
        private Long carbos;
        private Long proteins;
        private Long fats;

        public MealItem toMealItemEntity(MealItemDTO dto) {
            return MealItem.builder()
                    .name(dto.getName())
                    .calories(dto.getCalories())
                    .carbos(dto.getCarbos())
                    .proteins(dto.getProteins())
                    .fats(dto.getFats())
                    .build();
        }
    }

    @Getter
    public static class MealDTO {
        private MealType mealType;
        private String mealName;
        private List<MealItemDTO> mealItems;
        private Long price;

        public  Meal toMealEntity(MealDTO mealDTO, DailyMeal dailyMeal) {

            List<MealMealItemMapping> mappings = mealDTO.getMealItems().stream()
                    .map(mealItemDTO -> MealMealItemMapping.builder()
                            .mealItem(mealItemDTO.toMealItemEntity(mealItemDTO))
                            .build())
                    .toList();

            return Meal.builder()
                    .type(mealDTO.getMealType())
                    .mealName(mealDTO.getMealName())
                    .mealCalories(mealDTO.getMealItems().stream().mapToLong(MealItemDTO::getCalories).sum())
                    .price(mealDTO.getPrice())
                    .mealMealItemMappings(mappings)
                    .dailyMeal(dailyMeal)
                    .build();
        }
    }

    @Getter
    public static class DailyMealDTO {

        private int day;
        private LocalDate date;
        private List<MealDTO> meals;

        public DailyMeal toDailyMealEntity(DailyMealDTO dto, MealPlan mealPlan) {
            return DailyMeal.builder()
                    .date(dto.getDate())
                    .mealPlan(mealPlan)
                    .build();
        }
    }

    @Getter
    public static class MealPlanDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        private TargetNutritionDTO nutrition_goals;
        private List<DailyMealDTO> dailyMeals;

        public MealPlan toMealPlanEntity(MealPlanDTO dto, User user) {

            return MealPlan.builder()
                    .start_date(dto.startDate)
                    .end_date(dto.endDate)
                    .user(user)
                    .build();
        }
    }
}











