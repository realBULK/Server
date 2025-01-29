package umc7th.bulk.mealPlan.entity;

import lombok.Getter;

@Getter
public enum DayOfWeek {

    SUNDAY("일요일"),
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    ;

    private final String description;

    DayOfWeek(String description) {
        this.description = description;
    }
}
