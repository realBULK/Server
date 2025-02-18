package umc7th.bulk.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 Id의 유저가 존재하지 않습니다."),
    USER_NOT_AUTHENTICATED(HttpStatus.FORBIDDEN, "USER_NOT_AUTHENTICATED", "인증되지 않은 유저입니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER401", "비밀번호가 틀립니다."),
    INACTIVE_ACCOUNT(HttpStatus.FORBIDDEN, "MEMBER403", "비활성화된 계정입니다."),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER400", "이미 존재하는 사용자입니다."),

    // JWT 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "토큰이 만료되었습니다."),
    TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "TOKEN400", "토큰이 제공되지 않았습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
