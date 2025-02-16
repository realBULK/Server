package umc7th.bulk.emojiRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이모지 입력 요청 DTO")
public class EmojiRecordUpdateRequestDto {
    @Schema(description = "이모지 유형 ('heart' 또는 'special')", example = "special")
    private String emojiType;
}
