package umc7th.bulk.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import umc7th.bulk.review.entity.Review;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Integer rate;
    private String title;
    private String userEmail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime createdAt;

    private String content;

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getRate(),
                review.getReviewTitle(),
                review.getWriter().getEmail(),
                review.getCreatedAt(),
                review.getReviewContent()
                );
    }
}
