package umc7th.bulk.record.dto;

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
    private List<FoodResponseDto> foods;

    @Getter
    @Builder
    public static class FoodResponseDto {
        private Long foodId;
        private String foodName;
        private int quantity;
    }
}