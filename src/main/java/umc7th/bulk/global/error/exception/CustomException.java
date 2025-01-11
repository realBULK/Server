package umc7th.bulk.global.error.exception;

import lombok.Getter;
import umc7th.bulk.global.error.BaseErrorCode;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode code;

    public CustomException(BaseErrorCode code) {
        this.code = code;
    }
}
