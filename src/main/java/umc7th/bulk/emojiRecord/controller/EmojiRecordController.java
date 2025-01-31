package umc7th.bulk.emojiRecord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.emojiRecord.dto.EmojiRecordRequestDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordResponseDto;
import umc7th.bulk.emojiRecord.service.EmojiRecordService;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/emojis")
public class EmojiRecordController {

    private final EmojiRecordService emojiRecordService;

    @PostMapping
    @Operation(summary = "이모지 입력", description = "그룹 내 특정 사용자의 이모지를 입력합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = EmojiRecordResponseDto.class)))
    public CustomResponse<EmojiRecordResponseDto> createEmoji(
            @Parameter(description = "그룹 ID") @PathVariable Long groupId,
            @RequestBody EmojiRecordRequestDto requestDto) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, emojiRecordService.createEmoji(groupId, requestDto));
    }

    @PutMapping("/{emojiId}")
    @Operation(summary = "이모지 수정", description = "입력한 이모지의 종류를 수정합니다.")
    public CustomResponse<EmojiRecordResponseDto> updateEmoji(
            @Parameter(description = "그룹 ID") @PathVariable Long groupId,
            @Parameter(description = "이모지 기록 ID") @PathVariable Long emojiId,
            @RequestBody EmojiRecordRequestDto requestDto) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, emojiRecordService.updateEmoji(groupId, emojiId, requestDto));
    }

    @DeleteMapping("/{emojiId}")
    @Operation(summary = "이모지 삭제", description = "입력한 이모지를 삭제합니다.")
    public ResponseEntity<CustomResponse<Void>> deleteEmoji(
            @Parameter(description = "그룹 ID") @PathVariable Long groupId,
            @Parameter(description = "이모지 기록 ID") @PathVariable Long emojiId) {
        emojiRecordService.deleteEmoji(groupId, emojiId);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, null));
    }


}
