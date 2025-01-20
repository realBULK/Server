package umc7th.bulk.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
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
    public CustomResponse<?> saveQuestion(
            @RequestParam Long id,
            @RequestBody UserRequestDTO.UpdateUserDTO updateUserDTO) {

        User user = userQuestionService.updateUser(id, updateUserDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UpdateUserResponseDTO.from(user));
    }

    @GetMapping("/question/isDuplicated/{nickname}")
    @Operation(summary = "닉네임 중복 검사 API", description = "회원가입시 입력한 닉네임이 이미 존재하는지 확인")
    public CustomResponse<?> isDuplicatedNickname(@PathVariable("nickname") String nickname) {
        boolean checkResult = userQuestionService.nicknameCheck(nickname);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.DuplicateCheckResponseDTO.from(nickname, checkResult));
    }

    @PatchMapping("/question/report")
    @Operation(summary = "리포트 값 저장 API", description = "인공지능으로 도출한 섭취 칼로리, 단백질, 지방, 탄수화물 양 저장")
    public CustomResponse<?> saveReport(
            @RequestParam Long id,
            @RequestBody UserRequestDTO.UpdateUserReportDTO updateUserReportDTO) {

        User user = userQuestionService.updateUserReport(id, updateUserReportDTO);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserReportResponseDTO.from(user));
    }

    @GetMapping("/question/report")
    @Operation(summary = "저장된 레포트 값 조회 API", description = "저장된 칼로리, 탄단지 섭취권장량을 조회")
    public CustomResponse<?> getReport(@RequestParam Long id) {
        User user = userQuestionService.getUserReport(id);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, UserResponseDTO.UserReportResponseDTO.from(user));
    }
}
