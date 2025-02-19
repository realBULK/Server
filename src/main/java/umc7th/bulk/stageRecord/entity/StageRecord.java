package umc7th.bulk.stageRecord.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.group.entity.Group;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StageRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private int stageNumber;

    @Column(nullable = false)
    private int totalUsers;

    @Column(nullable = false)
    private int recordedUsers;

    @Column(nullable = false)
    private boolean isCompleted;

    private LocalDateTime completedAt;

    @Builder
    public StageRecord(Group group, int stageNumber, int totalUsers, int recordedUsers, boolean isCompleted) {
        this.group = group;
        this.stageNumber = stageNumber;
        this.totalUsers = totalUsers;
        this.recordedUsers = recordedUsers;
        this.isCompleted = isCompleted;
    }

    // 기록된 사용자 수 증가 로직
    public void increaseRecordedUsers() {
        this.recordedUsers++;
    }

    public void increaseTotalUsers() { this.totalUsers++; }

    // 스테이지 완료 처리 로직
    public void completeStage() {
        this.isCompleted = true;
        this.completedAt = LocalDateTime.now();
    }
}
