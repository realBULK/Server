package umc7th.bulk.mealPlan.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.exception.MealPlanErrorCode;
import umc7th.bulk.mealPlan.exception.MealPlanException;
import umc7th.bulk.mealPlan.repository.MealPlanRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanQueryServiceImpl implements MealPlanQueryService {

    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    @Override
    public MealPlan getMealPlan(Long userId, Long mealPlanId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return mealPlanRepository.findById(mealPlanId).orElseThrow(() -> new MealPlanException(MealPlanErrorCode.MEAL_PLAN_NOT_FOUND));

    }
}
