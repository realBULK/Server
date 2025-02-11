package umc7th.bulk.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "REVIEW4040", "해당 Review를 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "REVIEW4041", "해당 User Id를 찾을 수 없습니다."),
    NOT_FOUND_MEAL_ITEM(HttpStatus.NOT_FOUND, "REVIEW4043", "해당 Meal Item Id를 찾을 수 없습니다."),
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "REVIEW5040", "해당 음식에 대한 후기가 존재합니다.")
            ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
