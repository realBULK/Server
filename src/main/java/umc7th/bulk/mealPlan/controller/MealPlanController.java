package umc7th.bulk.mealPlan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.dailyMeal.service.command.DailyMealCommandService;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.meal.service.command.MealCommandService;
import umc7th.bulk.mealItem.service.command.MealItemCommandService;
import umc7th.bulk.mealMealItemMapping.service.command.MealMealItemMappingCommandService;
import umc7th.bulk.mealPlan.dto.MealPlanRequestDTO;
import umc7th.bulk.mealPlan.dto.MealPlanResponseDTO;
import umc7th.bulk.mealPlan.entity.MealPlan;
import umc7th.bulk.mealPlan.service.command.MealPlanCommandService;
import umc7th.bulk.mealPlan.service.query.MealPlanQueryService;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "일주일 식단 컨트롤러")
@RequestMapping("/api/mealPlan")
public class MealPlanController {

    private final MealPlanQueryService mealPlanQueryService;
    private final MealPlanCommandService mealPlanCommandService;
    private final DailyMealCommandService dailyMealCommandService;
    private final MealCommandService mealCommandService;
    private final MealMealItemMappingCommandService mealMealItemMappingCommandService;
    private final MealItemCommandService mealItemCommandService;
    private final UserService userService;

    @GetMapping("/mealPlanId")
    @Operation(summary = "유저의 일주일 식단 전체 조회 API")
    public CustomResponse<?> getMealWeekPlan(
            @RequestParam Long mealPlanId) {

        User user = userService.getAuthenticatedUserInfo();

        MealPlan mealPlan = mealPlanQueryService.getMealPlan(user.getId(), mealPlanId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, MealPlanResponseDTO.MealPlanGetResponseDTO.from(user.getId(), mealPlan));
    }

    @PostMapping("/")
    @Operation(summary = "유저의 일주일 식단 정보 생성 API")
    public CustomResponse<?> createUserMealPlan(
            @RequestBody MealPlanRequestDTO.MealPlanDTO mealPlanRequestDTO
    ) {
        User user = userService.getAuthenticatedUserInfo(); //로그인된 유저의 정보 가져오기
        mealPlanCommandService.createMealPlanDTO(mealPlanRequestDTO, user.getId());


        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
