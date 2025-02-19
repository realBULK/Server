package umc7th.bulk.stageRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import umc7th.bulk.group.entity.Group;
import umc7th.bulk.stageRecord.entity.StageRecord;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageRecordRepository extends JpaRepository<StageRecord, Long> {
    List<StageRecord> findByGroupGroupIdOrderByStageNumberAsc(Long groupId);

    Optional<StageRecord> findTopByGroupOrderByStageNumberDesc(Group group);

    @Query("SELECT sr FROM StageRecord sr WHERE sr.stageNumber = (SELECT MAX(s.stageNumber) FROM StageRecord s WHERE s.group = sr.group)")
    List<StageRecord> findLatestStageRecordsForAllGroups();
}
