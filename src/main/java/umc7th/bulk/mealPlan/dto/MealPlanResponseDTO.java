package umc7th.bulk.mealPlan.dto;

import lombok.*;
import umc7th.bulk.mealPlan.entity.MealPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static umc7th.bulk.dailyMeal.dto.DailyMealDTO.*;

public class MealPlanResponseDTO {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class MealPlanGetResponseDTO {

        private Long userId;
        private Long planMealId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<DailyMealInfoDTO> dailyMeals;

        public static MealPlanGetResponseDTO from(Long userId, MealPlan mealPlan) {
            return MealPlanGetResponseDTO.builder()
                    .userId(userId)
                    .planMealId(mealPlan.getId())
                    .startDate(mealPlan.getStartDate())
                    .endDate(mealPlan.getEndDate())
                    .dailyMeals(mealPlan.getDailyMeals().stream()
                            .map(dailyMeal -> new DailyMealInfoDTO(dailyMeal))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
