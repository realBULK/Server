package umc7th.bulk.dailyMeal.dto;

import lombok.Getter;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.meal.dto.MealDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DailyMealDTO {

    @Getter
    public static class DailyMealInfoDTO {
        private Long dailyMealId;
        private LocalDate date;
        private List<MealDTO.MealInfoDTO> meals;
        private Long dailyCalories;
        private Long dailyCarbos;
        private Long dailyProteins;
        private Long dailyFats;

        public DailyMealInfoDTO(DailyMeal dailyMeal) {
            dailyMealId = dailyMeal.getId();
            date = dailyMeal.getDate();
            meals = dailyMeal.getMeals().stream()
                    .map(meal -> new MealDTO.MealInfoDTO(meal))
                    .collect(Collectors.toList());
            dailyCalories = dailyMeal.getDailyCalories();
            dailyCarbos = dailyMeal.getDailyCarbos();
            dailyProteins = dailyMeal.getDailyProteins();
            dailyFats = dailyMeal.getDailyFats();
        }
    }
}
