package umc7th.bulk.mealPlan.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.dailyMeal.service.command.DailyMealService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.repository.MealPlanRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.exception.UserException;
import umc7th.bulk.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealPlanCommandServiceImpl implements MealPlanCommandService {

    private final MealPlanRepository mealPlanRepository;
    private final DailyMealService dailyMealService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MealPlan createMealPlanDTO(MealPlanRequestDTO.MealPlanDTO dto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        MealPlan mealPlan = mealPlanRepository.save(dto.toMealPlanEntity(dto, user));

        user.updateGoalNutrition(dto.getNutrition_goals());

        List<MealPlanRequestDTO.DailyMealDTO> dailyMealDTOList = dto.getDailyMeals();

        dailyMealDTOList.forEach(dailyMealDTO ->
                dailyMealService.createDailyMeal(mealPlan, dailyMealDTO));

        return mealPlan;
    }
}
