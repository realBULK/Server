package umc7th.bulk.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.global.error.GeneralErrorCode;
import umc7th.bulk.global.error.exception.CustomException;
import umc7th.bulk.group.dto.EmojiDto;
import umc7th.bulk.group.dto.GroupMapResponseDto;
import umc7th.bulk.group.dto.TodayMemberDto;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.group.repository.GroupRepository;
import umc7th.bulk.user.service.UserService;
import umc7th.bulk.userEmoji.repository.UserEmojiRepository;
import umc7th.bulk.stageRecord.dto.StageInfoDto;
import umc7th.bulk.stageRecord.entity.StageRecord;
import umc7th.bulk.stageRecord.repository.StageRecordRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final StageRecordRepository stageRecordRepository;
    private final UserRepository userRepository;
    private final UserEmojiRepository userEmojiRepository;
    private final UserService userService;

    public GroupMapResponseDto getGroupMap() {


        User currentUser = userService.getAuthenticatedUserInfo();

        Long groupId = currentUser.getGroup().getGroupId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.GROUP_NOT_FOUND_404));

        List<StageRecord> stageRecords = stageRecordRepository.findByGroupGroupIdOrderByStageNumberAsc(groupId);

        List<StageInfoDto> stageInfoList = stageRecords.stream()
                .map(stage -> new StageInfoDto(
                        stage.getStageNumber(),
                        stage.getRecordedUsers(),
                        stage.getTotalUsers()
                ))
                .collect(Collectors.toList());

        return GroupMapResponseDto.builder()
                .groupId(group.getGroupId())
                .currentStage(group.getCurrentStage())
                .stages(stageInfoList)
                .build();
    }

    public List<TodayMemberDto> getTodayMembers() {

        User currentUser = userService.getAuthenticatedUserInfo();
        Long groupId = currentUser.getGroup().getGroupId();

        // 그룹 존재 여부 확인
        groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.GROUP_NOT_FOUND_404));

        // 오늘의 팀원 찾기
        List<User> todayMembers = userRepository.findByGroupGroupIdAndRecordCompleteTrue(groupId);

        // 각 사용자가 받은 이모지 리스트 조회
        return todayMembers.stream()
                .map(user -> {
                    List<EmojiDto> emojis = userEmojiRepository.findByReceiver(user)
                            .stream()
                            .map(emoji -> new EmojiDto(emoji.getEmojiRecord().getEmojiType(), 1)) // 개수 계산 추가 필요
                            .toList();

                    return new TodayMemberDto(user.getId(), user.getNickname(), user.getBulkCharacter().getId(), emojis);
                })
                .toList();
    }

}
