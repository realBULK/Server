package umc7th.bulk.mealMealItemMapping.exception;

import umc7th.bulk.global.error.BaseErrorCode;
import umc7th.bulk.global.error.exception.CustomException;

public class MealMealItemMappingErrorException extends CustomException {

    public MealMealItemMappingErrorException(BaseErrorCode code) {
        super(code);
    }
}
