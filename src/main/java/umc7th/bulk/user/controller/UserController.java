package umc7th.bulk.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserRequestDTO;
import umc7th.bulk.user.dto.UserResponseDTO;
import umc7th.bulk.user.exception.UserErrorCode;
import umc7th.bulk.user.service.UserQuestionService;
import umc7th.bulk.user.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "회원가입 관련 컨트롤러")
public class UserController {

    private final UserService userService;
    private final UserQuestionService userQuestionService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @DeleteMapping("/unlink")
    public CustomResponse<?> unlinkUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        userService.unlinkUser(oAuth2User);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED);
    }

    @PatchMapping("/question")
    @Operation(summary = "입력한 question들을 저장하는 API", description = "회원가입시 입력한 내용들을 저장(업데이트! Patch 사용, 카카오 로그인 하면서 저장되는 값 제외)")
    public CustomResponse<?> saveQuestion(@RequestBody UserRequestDTO.UpdateUserDTO updateUserDTO) {
        User user = userService.getAuthenticatedUserInfo();
        User updateUser = userQuestionService.updateUser(user.getId(), updateUserDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UpdateUserResponseDTO.from(updateUser));
    }

    @GetMapping("/question/isDuplicated/{nickname}")
    @Operation(summary = "닉네임 중복 검사 API", description = "회원가입시 입력한 닉네임이 이미 존재하는지 확인")
    public CustomResponse<?> isDuplicatedNickname(@PathVariable("nickname") String nickname) {
        boolean checkResult = userQuestionService.nicknameCheck(nickname);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.DuplicateCheckResponseDTO.from(nickname, checkResult));
    }

    @PatchMapping("/question/report")
    @Operation(summary = "리포트 값 저장 API", description = "인공지능으로 도출한 섭취 칼로리, 단백질, 지방, 탄수화물 양 저장")
    public CustomResponse<?> saveReport(@RequestBody UserRequestDTO.UpdateUserReportDTO updateUserReportDTO) {
        User user = userService.getAuthenticatedUserInfo();
        User saveUserReport = userQuestionService.updateUserReport(user.getId(), updateUserReportDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserReportResponseDTO.from(saveUserReport));
    }

    @GetMapping("/question/report")
    @Operation(summary = "저장된 레포트 값 조회 API", description = "현재&목표 몸무게, 저장된 칼로리, 탄단지 섭취권장량을 조회 - 로그인 후 균형 잡힌 식단 페이지에 사용")
    public CustomResponse<?> getReport() {
        User user = userService.getAuthenticatedUserInfo(); //로그인된 유저의 정보 가져오기
        User getReportUser = userQuestionService.getUserReport(user.getId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserReportResponseDTO.from(getReportUser));
    }

    @GetMapping("/test")
    @Operation(summary = "현재 인증(로그인)된 유저id 조회 API", description = "일단 테스트용으로 두고 추후에 주석처리할 예정입니다.")
    public String getCurrentUser() {
        User user = userService.getAuthenticatedUserInfo();
        return "Hello, " + user.getId();
    }

    @GetMapping("/token")
    @Operation(summary = "현재 로그인한 사용자의 액세스 토큰과 리프레시 토큰을 반환하는 API")
    public CustomResponse<?> getAccessToken(@AuthenticationPrincipal OAuth2User oAuth2User) {

        if (oAuth2User == null) {
            System.out.println("❌ 인증된 사용자 없음 - SecurityContext에 인증 정보가 없음");
            return CustomResponse.fail(UserErrorCode.USER_NOT_AUTHENTICATED);
        }

        // 로그인한 사용자의 정보 가져오기
        String kakaoId = oAuth2User.getName();

        try {
            // 사용자 조회
            User user = userService.getUserByKakaoId(kakaoId);

            // OAuth2AuthorizedClient에서 토큰 가져오기
            Optional<OAuth2AuthorizedClient> optionalClient = Optional.ofNullable(
                    authorizedClientService.loadAuthorizedClient("kakao", oAuth2User.getName())
            );

            String accessToken = optionalClient.map(OAuth2AuthorizedClient::getAccessToken)
                    .map(token -> token.getTokenValue())
                    .orElse(null);

            String refreshToken = optionalClient.map(OAuth2AuthorizedClient::getRefreshToken)
                    .map(token -> token.getTokenValue())
                    .orElse(null);

            // 응답 데이터 구성
            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("accessToken", accessToken);
            tokenResponse.put("refreshToken", refreshToken);

            return CustomResponse.onSuccess(GeneralSuccessCode.OK, tokenResponse);
        } catch (Exception e) {
            System.out.println("❌ 사용자 정보 조회 실패: " + e.getMessage());
            return CustomResponse.fail(UserErrorCode.USER_NOT_FOUND);
        }
    }
}
