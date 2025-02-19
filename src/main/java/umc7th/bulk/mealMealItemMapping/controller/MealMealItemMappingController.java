package umc7th.bulk.mealMealItemMapping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.mealMealItemMapping.service.command.MealMealItemMappingCommandService;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.service.UserService;
import umc7th.bulk.user.service.command.UserCommandServiceImpl;

@RestController
@RequiredArgsConstructor
@Tag(name = "끼니와 관련 음식 컨트롤러")
@RequestMapping("/api/menu")
public class MealMealItemMappingController {

    private final MealMealItemMappingCommandService mappingCommandService;
    private final UserService userService;

    @DeleteMapping("/{mealMealItemMappingId}")
    @Operation(method = "DELETE", summary = "끼니에서의 특정 음식 삭제 API")
    @Parameter(name = "mealMealItemMappingId", description = "삭제 해야하는 끼니-음식 ID 입력.")
    public CustomResponse<?> deleteMealMealItemMappingById(@PathVariable("mealMealItemMappingId") Long mealMealItemMappingId) {

        User user = userService.getAuthenticatedUserInfo();

        mappingCommandService.deleteMealMealItemMapping(user, mealMealItemMappingId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK);
    }
}
