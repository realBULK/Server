package umc7th.bulk.stageRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.stageRecord.entity.StageRecord;

import java.util.List;

public interface StageRecordRepository extends JpaRepository<StageRecord, Long> {
    List<StageRecord> findByGroupGroupIdOrderByStageNumberAsc(Long groupId);
}
