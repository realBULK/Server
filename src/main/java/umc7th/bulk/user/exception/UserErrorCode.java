package umc7th.bulk.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 Id의 유저가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "USER_NOT_AUTHENTICATED", "인증되지 않은 유저입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
