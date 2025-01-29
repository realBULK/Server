package umc7th.bulk.mealPlan.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class MealPlanException extends CustomException {
    public MealPlanException(BaseErrorCode code) {
        super(code);
    }
}
