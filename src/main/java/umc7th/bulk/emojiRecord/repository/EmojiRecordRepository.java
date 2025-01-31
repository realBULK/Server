package umc7th.bulk.emojiRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.emojiRecord.entity.EmojiRecord;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.user.domain.User;

import java.util.List;

public interface EmojiRecordRepository extends JpaRepository<EmojiRecord, Long> {
    List<EmojiRecord> findByGroup(Group group);
}
