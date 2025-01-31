package umc7th.bulk.dailyMeal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum DailyMealErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "DAILY_MEAL_NOT_FOUND", "해당 Id의 하루 식단이 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
