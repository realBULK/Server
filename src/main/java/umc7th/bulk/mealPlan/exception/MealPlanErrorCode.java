package umc7th.bulk.mealPlan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum MealPlanErrorCode implements BaseErrorCode {

    MEAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "MEAL_PLAN_NOT_FOUND", "해당 Id의 일주일 식단이 없습니다."),
    USER_MEAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_MEAL_PLAN_NOT_FOUND", "유저의 mealPlan(식단)이 존재 하지 않습니다.")
    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}

