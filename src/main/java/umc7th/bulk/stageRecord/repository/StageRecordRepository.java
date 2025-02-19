package umc7th.bulk.stageRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.stageRecord.entity.StageRecord;

import java.util.List;
import java.util.Optional;

public interface StageRecordRepository extends JpaRepository<StageRecord, Long> {
    List<StageRecord> findByGroupGroupIdOrderByStageNumberAsc(Long groupId);

    Optional<StageRecord> findTopByGroupOrderByStageNumberDesc(Group group);
}
