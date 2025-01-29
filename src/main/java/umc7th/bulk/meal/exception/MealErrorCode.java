package umc7th.bulk.meal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@AllArgsConstructor
@Getter
public enum MealErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "MEAL404", "유저의 식단을 찾지 못했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
