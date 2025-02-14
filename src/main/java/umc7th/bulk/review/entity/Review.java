package umc7th.bulk.review.entity;

import jakarta.persistence.*;
import lombok.*;
import umc7th.bulk.global.BaseTimeEntity;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.user.domain.User;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private Integer rate; // 평점 (1~5)

    @Column(name = "review_title", length = 100)
    private String reviewTitle; // 후기 제목


    @Column(name = "review_content", length = 200)
    private String reviewContent; // 후기 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_item_id", nullable = false)
    private MealItem mealItem; // 음식

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer; // 작성자

    // 평점에 따라 제목을 자동으로 설정하는 메서드
    private static String generateTitle(int rate) {
        return switch (rate) {
            case 1 -> "이 식품은 추천하지 않을 것 같아요.";
            case 2 -> "증량에는 약간 부족했어요. 개선이 필요해요.";
            case 3 -> "효과는 있었지만, 기대에 조금 못 미쳤어요.";
            case 4 -> "대체로 만족스러웠어요. 다시 먹고 싶어요.";
            case 5 -> "이 식품 덕분에 증량이 쉬워졌어요!";
            default -> "평점 없음";
        };
    }

    @PostLoad
    public void setGeneratedTitle() {
        if (this.getReviewTitle() == null) {
            this.reviewTitle = generateTitle(this.rate);
        }
    }
}
