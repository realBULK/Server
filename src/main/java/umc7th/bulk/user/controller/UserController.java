package umc7th.bulk.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.dto.UserRequestDTO;
import umc7th.bulk.user.dto.UserResponseDTO;
import umc7th.bulk.user.service.UserQuestionService;
import umc7th.bulk.user.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "회원가입 관련 컨트롤러")
public class UserController {

    private final UserService userService;
    private final UserQuestionService userQuestionService;

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
    @Operation(summary = "저장된 레포트 값 조회 API", description = "저장된 칼로리, 탄단지 섭취권장량을 조회")
    public CustomResponse<?> getReport() {
        User user = userService.getAuthenticatedUserInfo(); //로그인된 유저의 정보 가져오기
        User getReportUser = userQuestionService.getUserReport(user.getId());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserReportResponseDTO.from(getReportUser));
    }

    @GetMapping("/test")
    public String getCurrentUser() {
        User user = userService.getAuthenticatedUserInfo();
        return "Hello, " + user.getId();
    }
}
