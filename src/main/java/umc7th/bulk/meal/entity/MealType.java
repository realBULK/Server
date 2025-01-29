package umc7th.bulk.meal.entity;

import lombok.Getter;

@Getter
public enum MealType {

    BREAKFAST("아침"),
    LUNCH("점심"),
    DINNER("저녁"),
    SNACK("간식"),
    ;

    private final String description;

    MealType(String description) {
        this.description = description;
    }
}
