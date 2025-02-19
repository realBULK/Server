package umc7th.bulk.record.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordCompleteResetService {

    private final UserRepository userRepository;

    /**
     * 매일 자정에 record_complete 값을 false로 초기화하는 배치 작업
     */

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetRecordComplete() {
        log.info("매일 자정 record_complete 값 초기화 시작...");
        int updatedCount = userRepository.resetRecordComplete();
        log.info("✅ 초기화 완료: {}명의 record_complete 값을 false로 변경", updatedCount);
    }
}
