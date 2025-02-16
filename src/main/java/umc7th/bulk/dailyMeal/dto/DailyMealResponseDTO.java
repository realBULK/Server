package umc7th.bulk.dailyMeal.dto;

import lombok.*;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.meal.dto.MealDTO;

import java.time.LocalDate;
import java.util.List;

public class DailyMealResponseDTO {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    // 오늘 식단 조회 응답 dto
    public static class DailyMealGetResponseDTO {
        private Long dailyMealId;
        private LocalDate date;
        private List<MealDTO.MealNutritionDTO> meals;

        public static DailyMealGetResponseDTO from(DailyMeal dailyMeal) {
            return DailyMealGetResponseDTO.builder()
                    .dailyMealId(dailyMeal.getId())
                    .date(dailyMeal.getDate())
                    .meals(dailyMeal.getMeals().stream()
                            .map(meal -> new MealDTO.MealNutritionDTO(meal))
                            .toList())
                    .build();
        }
    }
}
