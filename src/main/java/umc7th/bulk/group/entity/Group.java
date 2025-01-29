package umc7th.bulk.group.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "`group`")
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = true, length = 100)
    private String groupName;

    @Column(nullable = false)
    private int currentStage = 1;

    private LocalDateTime endDate;

    public Group(String groupName, int currentStage, LocalDateTime endDate) {
        this.groupName = groupName;
        this.currentStage = currentStage;
        this.endDate = endDate;
    }

    // 스테이지 증가 로직
    public void advanceStage() {
        this.currentStage++;
    }
}
