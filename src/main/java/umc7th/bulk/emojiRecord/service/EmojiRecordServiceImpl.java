package umc7th.bulk.emojiRecord.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.emojiRecord.dto.EmojiRecordRequestDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordResponseDto;
import umc7th.bulk.emojiRecord.dto.EmojiRecordUpdateRequestDto;
import umc7th.bulk.emojiRecord.entity.EmojiRecord;
import umc7th.bulk.emojiRecord.repository.EmojiRecordRepository;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.group.repository.GroupRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;
import umc7th.bulk.user.service.UserService;
import umc7th.bulk.userEmoji.entity.UserEmoji;
import umc7th.bulk.userEmoji.repository.UserEmojiRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmojiRecordServiceImpl implements EmojiRecordService {

    private final EmojiRecordRepository emojiRecordRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserEmojiRepository userEmojiRepository;
    private final UserService userService;

    @Transactional
    public EmojiRecordResponseDto createEmoji(EmojiRecordRequestDto requestDto) {

        User sender = userService.getAuthenticatedUserInfo();

        Long groupId = sender.getGroup().getGroupId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        User receiver = userRepository.findById(requestDto.getReceiverUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid receiver user ID"));

        // EmojiRecord 저장
        EmojiRecord emojiRecord = EmojiRecord.builder()
                .group(group)
                .emojiType(requestDto.getEmojiType())
                .build();
        EmojiRecord savedEmoji = emojiRecordRepository.save(emojiRecord);

        // UserEmoji 저장 (sender, receiver 관계 설정)
        UserEmoji userEmoji = UserEmoji.builder()
                .emojiRecord(savedEmoji)
                .sender(sender)
                .receiver(receiver)
                .build();
        userEmojiRepository.save(userEmoji);

        return EmojiRecordResponseDto.builder()
                .emojiRecordId(savedEmoji.getEmojiRecordId())
                .emojiType(savedEmoji.getEmojiType())
                .build();
    }

    @Transactional
    public EmojiRecordResponseDto updateEmoji(Long emojiId, EmojiRecordUpdateRequestDto requestDto) {
        EmojiRecord emojiRecord = emojiRecordRepository.findById(emojiId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid emoji ID"));

        emojiRecord.setEmojiType(requestDto.getEmojiType());

        return EmojiRecordResponseDto.builder()
                .emojiRecordId(emojiRecord.getEmojiRecordId())
                .emojiType(emojiRecord.getEmojiType())
                .build();
    }

    @Transactional
    public void deleteEmoji(Long emojiId) {
        EmojiRecord emojiRecord = emojiRecordRepository.findById(emojiId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid emoji ID"));
        userEmojiRepository.deleteByEmojiRecord(emojiRecord);
        emojiRecordRepository.delete(emojiRecord);
        emojiRecordRepository.flush();
    }


}