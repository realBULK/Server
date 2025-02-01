package umc7th.bulk.userEmoji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.emojiRecord.entity.EmojiRecord;
import umc7th.bulk.userEmoji.entity.UserEmoji;
import umc7th.bulk.user.domain.User;

import java.util.List;

public interface UserEmojiRepository extends JpaRepository<UserEmoji, Long> {
    List<UserEmoji> findByReceiver(User receiver); // 특정 사용자가 받은 이모지 조회
    int countByReceiverAndEmojiRecord_EmojiRecordIdIn(User receiver, List<Long> emojiRecordIds);
    void deleteByEmojiRecord(EmojiRecord emojiRecord);
}
