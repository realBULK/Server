package umc7th.bulk.dailyMeal.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc7th.bulk.dailyMeal.dto.DailyMealResponseDTO;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.dailyMeal.exception.DailyMealErrorCode;
import umc7th.bulk.dailyMeal.exception.DailyMealException;
import umc7th.bulk.dailyMeal.repository.DailyMealRepository;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.meal.service.command.MealCommandService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.repository.MealPlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyMealServiceImpl implements DailyMealService {

    private final DailyMealRepository dailyMealRepository;
    private final MealCommandService mealCommandService;
    private final MealPlanRepository mealPlanRepository;

    @Override
    public DailyMeal createDailyMeal(MealPlan mealPlan, MealPlanRequestDTO.DailyMealDTO dto) {

        /*MealPlan mealPlan = mealPlanRepository.findById(mealPlanId).orElseThrow(() ->
                new MealPlanException(MealErrorCode.NOT_FOUND));*/

        DailyMeal dailyMeal = dailyMealRepository.save(dto.toDailyMealEntity(dto, mealPlan));

        List<MealPlanRequestDTO.MealDTO> mealDTOList = dto.getMeals();

        mealDTOList.forEach(mealDTO -> mealCommandService.createMeal(dailyMeal, mealDTO));

        return dailyMeal;
    }

    @Override
    public DailyMealResponseDTO.DailyMealGetResponseDTO getDailyMeal(Long userId, Long dailyMealId) {

        DailyMeal dailyMeal = dailyMealRepository.findById(dailyMealId).orElseThrow(() ->
                new DailyMealException(DailyMealErrorCode.NOT_FOUND));

        if (!dailyMeal.getMealPlan().getUser().getId().equals(userId)) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }

        return DailyMealResponseDTO.DailyMealGetResponseDTO.from(dailyMeal);
    }
}
