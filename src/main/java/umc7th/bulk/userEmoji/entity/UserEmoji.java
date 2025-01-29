package umc7th.bulk.userEmoji.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.emojiRecord.entity.EmojiRecord;
import umc7th.bulk.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEmoji extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userEmojiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_record_id", nullable = false)
    private EmojiRecord emojiRecord; // 이모지 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User sender; // 이모지 입력한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User receiver; // 이모지 받은 사용자

}
