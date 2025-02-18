package umc7th.bulk.global.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN401", "토큰이 유효하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    public <T> CustomResponse<T> getResponse() {
        return null;
    }
}
