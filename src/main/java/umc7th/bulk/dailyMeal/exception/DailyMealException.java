package umc7th.bulk.dailyMeal.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class DailyMealException extends CustomException {
    public DailyMealException(BaseErrorCode code) {
        super(code);
    }
}
