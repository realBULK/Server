package umc7th.bulk.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.meal.entity.Meal;
import umc7th.bulk.mealItem.entity.MealItem;
import umc7th.bulk.mealItem.repository.MealItemRepository;
import umc7th.bulk.review.dto.MealItemReviewResponseDto;
import umc7th.bulk.review.dto.ReviewRequestDto;
import umc7th.bulk.review.dto.ReviewResponseDto;
import umc7th.bulk.review.entity.Review;
import umc7th.bulk.review.exception.ReviewErrorCode;
import umc7th.bulk.review.exception.ReviewErrorException;
import umc7th.bulk.review.repository.ReviewRepository;
import umc7th.bulk.user.domain.User;
import umc7th.bulk.user.repository.UserRepository;
import umc7th.bulk.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final MealItemRepository mealItemRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    // 리뷰 생성 메서드
    public ReviewResponseDto createReview(Long mealItemId, ReviewRequestDto requestDto) {
        User currentUser = userService.getAuthenticatedUserInfo();

        MealItem mealItem = mealItemRepository.findById(mealItemId)
                .orElseThrow(() -> new ReviewErrorException(ReviewErrorCode.NOT_FOUND_MEAL_ITEM));

        boolean exists = reviewRepository.existsByMealItemIdAndWriter(mealItemId, currentUser);
        if (exists) {
            throw new ReviewErrorException(ReviewErrorCode.ALREADY_REVIEWED);
        }

        Review review = Review.builder()
                .mealItem(mealItem)
                .rate(requestDto.getRate())
                .reviewContent(requestDto.getContent())
                .writer(currentUser)
                .build();

        reviewRepository.save(review);
        updateReviewStats(mealItemId);

        return ReviewResponseDto.from(review);
    }

    // 리뷰 리스트 조회 메서드
    public MealItemReviewResponseDto getReviewsByMealItemId(Long mealItemId) {
        MealItem mealItem = mealItemRepository.findById(mealItemId)
                .orElseThrow(() -> new ReviewErrorException(ReviewErrorCode.NOT_FOUND_MEAL_ITEM));

        List<Review> reviews = reviewRepository.findByMealItemIdOrderByCreatedAtDesc(mealItemId);

        return MealItemReviewResponseDto.from(mealItem, reviews);
    }

    // 평균 평점 조회 메서드
    public Double getAverageRateByMealItemId(Long mealItemId) {
        return reviewRepository.findAverageRateByMealItemId(mealItemId);
    }

    // 평균 평점, 후기 개수 업데이트 메서드
    public void updateReviewStats(Long mealItemId) {
        List<Review> reviews = reviewRepository.findByMealItemIdOrderByCreatedAtDesc(mealItemId);

        double avgRating = reviews.stream().mapToInt(Review::getRate).average().orElse(0.0);
        long reviewCount = reviews.size();

        // 음식 엔티티 조회
        MealItem mealItem = mealItemRepository.findById(mealItemId)
                .orElseThrow(() -> new ReviewErrorException(ReviewErrorCode.NOT_FOUND_MEAL_ITEM));

        // 음식 엔티티 업데이트
        mealItem.updateReviewStats(reviewCount, avgRating);
        mealItemRepository.save(mealItem);
    }

}
