package umc7th.bulk.review.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class ReviewErrorException extends CustomException {
    public ReviewErrorException(BaseErrorCode code) {
        super(code);
    }
}
