package umc7th.bulk.meal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.meal.dto.MealResponseDTO;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.meal.service.query.MealQueryService;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "식단 관련 컨트롤러")
@RequestMapping("/api/menu")
public class MealController {

    private final MealQueryService mealQueryService;
    private final UserService userService;

    @GetMapping("/{dailyMealId}")
    @Operation(method = "GET", summary = "식단 상세 조회 API")
    public CustomResponse<?> getMealById(
            @PathVariable("dailyMealId") Long dailyMealId,
            @RequestParam MealType type,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "pageSize", defaultValue = "2") int pageSize) {

        User user = userService.getAuthenticatedUserInfo();
        MealResponseDTO.MealPreviewDTO mealItems = mealQueryService.getMealItems(user.getId(),dailyMealId, type, cursorId, pageSize);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, mealItems);
    }

}
