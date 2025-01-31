package umc7th.bulk.record.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import umc7th.bulk.meal.entity.MealType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RecordRequestDto {
    private LocalDate date;
    private MealType mealType;
    private String foodPhoto;
}