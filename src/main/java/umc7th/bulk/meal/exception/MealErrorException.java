package umc7th.bulk.meal.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class MealErrorException extends CustomException {
    public MealErrorException(BaseErrorCode code) {
        super(code);
    }
}
