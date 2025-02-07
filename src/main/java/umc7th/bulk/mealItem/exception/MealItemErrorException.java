package umc7th.bulk.mealItem.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class MealItemErrorException extends CustomException {
    public MealItemErrorException(BaseErrorCode code) {
        super(code);
    }
}
