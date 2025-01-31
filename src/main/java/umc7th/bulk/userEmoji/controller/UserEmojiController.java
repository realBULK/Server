package umc7th.bulk.userEmoji.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.userEmoji.service.UserEmojiService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/emojis")
public class UserEmojiController {

    private final UserEmojiService userEmojiService;

    @GetMapping("/count/{receiverUserId}")
    @Operation(summary = "특정 사용자가 받은 이모지 개수 조회", description = "특정 그룹 내 특정 사용자가 받은 이모지의 총 개수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public CustomResponse<Integer> getUserEmojiCount(
            @Parameter(description = "그룹 ID") @PathVariable Long groupId,
            @Parameter(description = "이모지를 받은 사용자 ID") @PathVariable Long receiverUserId) {
        int count = userEmojiService.countEmojisByUser(groupId, receiverUserId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, count);
    }
}
