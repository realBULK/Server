package umc7th.bulk.userEmoji.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.emojiRecord.entity.EmojiRecord;
import umc7th.bulk.emojiRecord.repository.EmojiRecordRepository;
import umc7th.bulk.userEmoji.repository.UserEmojiRepository;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.group.repository.GroupRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEmojiServiceImpl implements UserEmojiService {

    private final UserEmojiRepository userEmojiRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final EmojiRecordRepository emojiRecordRepository;

    @Transactional(readOnly = true)
    public int countEmojisByUser(Long receiverUserId) {
        User receiver = userRepository.findById(receiverUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Long groupId = receiver.getGroup().getGroupId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        // 해당 그룹에서 생성된 모든 EmojiRecord 조회
        List<Long> emojiRecordIds = emojiRecordRepository.findByGroup(group)
                .stream()
                .map(EmojiRecord::getEmojiRecordId)
                .collect(Collectors.toList());

        if (emojiRecordIds.isEmpty()) {
            return 0;
        }

        // UserEmoji 테이블에서 해당 사용자가 받은 이모지 개수 조회
        return userEmojiRepository.countByReceiverAndEmojiRecord_EmojiRecordIdIn(receiver, emojiRecordIds);
    }
}