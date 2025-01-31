package umc7th.bulk.emojiRecord.service;

import umc7th.bulk.emojiRecord.dto.EmojiRecordRequestDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordResponseDto;

public interface EmojiRecordService {
    EmojiRecordResponseDto createEmoji(Long groupId, EmojiRecordRequestDto requestDto);
    EmojiRecordResponseDto updateEmoji(Long groupId, Long emojiId, EmojiRecordRequestDto requestDto);
    void deleteEmoji(Long groupId, Long emojiId);
}
