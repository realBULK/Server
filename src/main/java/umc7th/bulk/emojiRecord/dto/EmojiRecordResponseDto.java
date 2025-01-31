package umc7th.bulk.emojiRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이모지 응답 DTO")
public class EmojiRecordResponseDto {

    @Schema(description = "이모지 기록 ID", example = "1")
    private Long emojiRecordId;

    @Schema(description = "이모지 유형", example = "heart")
    private String emojiType;
}
