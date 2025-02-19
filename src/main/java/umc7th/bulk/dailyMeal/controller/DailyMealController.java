package umc7th.bulk.dailyMeal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import umc7th.bulk.dailyMeal.service.command.DailyMealService;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "오늘 식단 관련 컨트롤러")
public class DailyMealController {

    private final DailyMealService dailyMealService;
    private final UserService userService;

    @GetMapping("/api/dailyMeal/{dailyMealId}")
    public CustomResponse<?> getDailyMeal(@PathVariable("dailyMealId") Long dailyMealId) {

        User user = userService.getAuthenticatedUserInfo();

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, dailyMealService.getDailyMeal(user.getId(), dailyMealId));
    }
}
