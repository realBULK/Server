package umc7th.bulk.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.review.entity.Review;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MealItemReviewResponseDto {
    private String name;
    private Double averageRate;
    private Long reviewCount;
    private List<ReviewResponseDto> reviews;

    public static MealItemReviewResponseDto from(MealItem mealItem, List<Review> reviews) {
        List<ReviewResponseDto> reviewResponses = reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());

        return new MealItemReviewResponseDto(
                mealItem.getName(),
                mealItem.getGrade(),
                mealItem.getGradePeopleNum(),
                reviewResponses
        );
    }
}
