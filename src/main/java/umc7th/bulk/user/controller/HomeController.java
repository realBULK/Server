package umc7th.bulk.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserResponseDTO;
import umc7th.bulk.user.service.UserService;

import static umc7th.bulk.user.dto.UserDTO.*;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/home/info")
    @Operation(summary = "홈 api", description = "홈 화면의 정보가 보여진다. 캐릭터 정보와 목표 칼로리, 탄단지")
    public CustomResponse<?> home() {

        User user = userService.getAuthenticatedUserInfo(); //로그인된 유저의 정보 가져오기

        CharacterDTO bulkCharacterDTO = userService.getCharacter(user.getId());
        UserNutritionDTO curNutritionDTO = userService.getUserNutrition(user.getId());

        UserResponseDTO.UserInfoDTO response = UserResponseDTO.UserInfoDTO.from(bulkCharacterDTO, curNutritionDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }
}
