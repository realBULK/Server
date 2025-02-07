package umc7th.bulk.emojiRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이모지 입력 요청 DTO")
public class EmojiRecordRequestDto {

    @Schema(description = "이모지를 입력하는 사용자 ID", example = "1")
    private Long senderUserId;

    @Schema(description = "이모지를 받는 사용자 ID", example = "3")
    private Long receiverUserId;

    @Schema(description = "이모지 유형 ('heart' 또는 'special')", example = "special")
    private String emojiType;
}
