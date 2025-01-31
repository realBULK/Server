package umc7th.bulk.emojiRecord.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.group.entity.Group;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmojiRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emojiRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group; // 이모지가 속한 그룹

    // TODO : 이모지 유형 바뀔 경우 수정 필요 (ex. ENUM)
    @Column(nullable = false, length = 20)
    private String emojiType; // 이모지 유형

    public void setEmojiType(String emojiType) {
        this.emojiType = emojiType;
    }
}
