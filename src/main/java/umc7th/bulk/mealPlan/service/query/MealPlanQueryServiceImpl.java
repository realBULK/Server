package umc7th.bulk.mealPlan.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.exception.MealPlanErrorCode;
import umc7th.bulk.mealPlan.exception.MealPlanException;
import umc7th.bulk.mealPlan.repository.MealPlanRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanQueryServiceImpl implements MealPlanQueryService {

    private final MealPlanRepository mealPlanRepository;
    private final UserRepository userRepository;

    @Override
    public MealPlan getMealPlan(Long userId, Long mealPlanId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId).orElseThrow(() -> new MealPlanException(MealPlanErrorCode.MEAL_PLAN_NOT_FOUND));
        if (!mealPlan.getUser().getId().equals(user.getId()) ) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }
        return mealPlan;
    }

    @Override
    public List<Long> getMealPlans(User user) {

        List<MealPlan> userMealPlans = mealPlanRepository.findByUserId(user.getId());
        if (userMealPlans.isEmpty()) {
            throw new MealPlanException(MealPlanErrorCode.USER_MEAL_PLAN_NOT_FOUND);
        }

        return userMealPlans.stream()
                .map(mealPlan -> mealPlan.getId())
                .toList();

    }
}
