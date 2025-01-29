package umc7th.bulk.mealPlan.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.mealPlan.dto.MealPlanResponseDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.service.query.MealPlanQueryService;
import umc7th.bulk.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "일주일 식단 컨트롤러")
@RequestMapping("/api/mealPlan")
public class MealPlanController {

    private final MealPlanQueryService mealPlanQueryService;
    private final UserService userService;

    @GetMapping("/{mealPlanId}")
    public CustomResponse<?> getMealWeekPlan(
            @RequestParam Long userId,
            @RequestParam Long mealPlanId) {

        MealPlan mealPlan = mealPlanQueryService.getMealPlan(userId, mealPlanId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, MealPlanResponseDTO.MealPlanGetResponseDTO.from(userId, mealPlan));
    }
}
