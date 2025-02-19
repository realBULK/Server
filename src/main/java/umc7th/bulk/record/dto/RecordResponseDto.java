package umc7th.bulk.record.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import umc7th.bulk.meal.entity.MealType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RecordResponseDto {

    private Long recordId;
    private boolean ateOnPlan;
    private LocalDate date;
    private MealType mealType;
    private Long totalCalories;
    private Long totalCarbs;
    private Long totalProtein;
    private Long totalFat;
    private String foodPhoto;
    private String gptAnalysis;
    private List<FoodResponse> foods;

    @Getter
    @Builder
    public static class FoodResponse {
        private String foodName;
        private int quantity;
        private Double grade;
        private Long gradePeopleNum;
        private Long carbos;
        private Long proteins;
        private Long fats;
    }

    @Getter
    @Builder
    public static class TodaySummary {
        private LocalDate date;
        private Long totalCalories;
        private Long totalCarbs;
        private Long totalProtein;
        private Long totalFat;
    }
}