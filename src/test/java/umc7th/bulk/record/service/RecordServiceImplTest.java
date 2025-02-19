package umc7th.bulk.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.group.repository.GroupRepository;
import umc7th.bulk.stageRecord.entity.StageRecord;
import umc7th.bulk.stageRecord.repository.StageRecordRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecordServiceImplTest {

    @InjectMocks
    private RecordServiceImpl recordService; // 테스트할 대상

    @Mock
    private UserRepository userRepository;

    @Mock
    private StageRecordRepository stageRecordRepository;

    @Mock
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStageAdvancementWhenFiveUsersRecord() {
        // given
        Group group = Group.builder().groupId(1L).groupName("Test Group").currentStage(1).build();

        User user1 = User.builder().id(1L).group(group).recordComplete(true).build();
        User user2 = User.builder().id(2L).group(group).recordComplete(true).build();
        User user3 = User.builder().id(3L).group(group).recordComplete(true).build();
        User user4 = User.builder().id(4L).group(group).recordComplete(true).build();
        User user5 = User.builder().id(5L).group(group).recordComplete(true).build();

        StageRecord currentStage = StageRecord.builder()
                .group(group)
                .stageNumber(1)
                .totalUsers(10)
                .recordedUsers(4) // 현재 4명 기록 완료 (5명 달성 시 스테이지 변경)
                .isCompleted(false)
                .build();

        // when
        when(userRepository.countByGroupAndRecordCompleteTrue(group)).thenReturn(5);
        when(stageRecordRepository.findTopByGroupOrderByStageNumberDesc(group)).thenReturn(Optional.of(currentStage));

        recordService.checkAndAdvanceStage(group);

        // then
        assertTrue(currentStage.isCompleted()); // ✅ 기존 스테이지가 완료됐는지 확인
        verify(stageRecordRepository, times(2)).save(any(StageRecord.class)); // ✅ 새로운 스테이지 저장 확인
        verify(userRepository, times(1)).countByGroupAndRecordCompleteTrue(group); // ✅ 기록 완료 인원 체크 확인
        verify(groupRepository, times(1)).save(group);
    }

}