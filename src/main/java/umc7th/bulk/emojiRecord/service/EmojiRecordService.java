package umc7th.bulk.emojiRecord.service;

import umc7th.bulk.emojiRecord.dto.EmojiRecordRequestDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordResponseDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordUpdateRequestDto;

public interface EmojiRecordService {
    EmojiRecordResponseDto createEmoji(EmojiRecordRequestDto requestDto);
    EmojiRecordResponseDto updateEmoji(Long emojiId, EmojiRecordUpdateRequestDto requestDto);
    void deleteEmoji(Long emojiId);
}
