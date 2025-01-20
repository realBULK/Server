package umc7th.bulk.user.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class UserException extends CustomException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
