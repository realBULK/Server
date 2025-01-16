package umc7th.bulk.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.user.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/unlink")
    public CustomResponse<?> unlinkUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        userService.unlinkUser(oAuth2User);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED);
    }
}
