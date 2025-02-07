package umc7th.bulk.mealItem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum MealItemErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "MealItem_NOT_FOUND", "해당 MealItem id가 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
