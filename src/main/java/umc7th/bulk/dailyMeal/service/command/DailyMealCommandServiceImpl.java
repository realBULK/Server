package umc7th.bulk.dailyMeal.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc7th.bulk.dailyMeal.entity.DailyMeal;
import umc7th.bulk.dailyMeal.repository.DailyMealRepository;
import umc7th.bulk.meal.exception.MealErrorCode;
import umc7th.bulk.meal.service.command.MealCommandService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.exception.MealPlanException;
import umc7th.bulk.mealPlan.repository.MealPlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyMealCommandServiceImpl implements DailyMealCommandService{

    private final DailyMealRepository dailyMealRepository;
    private final MealCommandService mealCommandService;
    private final MealPlanRepository mealPlanRepository;

    @Override
    public DailyMeal createDailyMeal(Long mealPlanId, MealPlanRequestDTO.DailyMealDTO dto) {

        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId).orElseThrow(() ->
                new MealPlanException(MealErrorCode.NOT_FOUND));

        DailyMeal dailyMeal = dailyMealRepository.save(dto.toDailyMealEntity(dto, mealPlan));

        List<MealPlanRequestDTO.MealDTO> mealDTOList = dto.getMeals();

        mealDTOList.forEach(mealDTO -> mealCommandService.createMeal(dailyMeal.getId(), mealDTO));

        return dailyMeal;
    }
}
