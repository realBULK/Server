package umc7th.bulk.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RecordCompleteResetServiceTest {

    @InjectMocks
    private RecordCompleteResetService recordCompleteResetService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResetRecordComplete() {
        // given
        User user1 = User.builder().id(1L).email("user1@example.com").recordComplete(true).build();
        User user2 = User.builder().id(2L).email("user2@example.com").recordComplete(true).build();
        List<User> users = Arrays.asList(user1, user2);

        // when
        when(userRepository.resetRecordComplete()).thenReturn(2);

        recordCompleteResetService.resetRecordComplete();

        // then
        verify(userRepository, times(1)).resetRecordComplete(); // 1번 실행됐는지 검증
    }


}