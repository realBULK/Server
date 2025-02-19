package umc7th.bulk.group.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

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
    @Builder.Default
    private int currentStage = 1;

    private LocalDateTime endDate;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<User> members;

    public boolean isFull() {
        return members != null & members.size() >= 10;
    }

    public void addMember(User user) {
        if (isFull()) {
            throw new RuntimeException("그룹이 이미 가득 찼습니다.");
        }
        members.add(user);
        user.setGroup(this);
    }

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
