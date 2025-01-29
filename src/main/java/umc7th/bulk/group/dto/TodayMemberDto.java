package umc7th.bulk.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TodayMemberDto {
    private Long userId;
    private String nickname;
    private Long characterId;
    private List<EmojiDto> emojis;
}
