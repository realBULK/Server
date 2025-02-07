package umc7th.bulk.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import umc7th.bulk.global.apiPayload.CustomResponse;
import umc7th.bulk.global.success.GeneralSuccessCode;
import umc7th.bulk.review.dto.MealItemReviewResponseDto;
import umc7th.bulk.review.dto.ReviewRequestDto;
import umc7th.bulk.review.dto.ReviewResponseDto;
import umc7th.bulk.review.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 API
    @PostMapping("/{mealItemId}")
    public ResponseEntity<CustomResponse<ReviewResponseDto>> createReview(
            @PathVariable Long mealItemId,
            @RequestBody @Valid ReviewRequestDto requestDto) {

        ReviewResponseDto response = reviewService.createReview(mealItemId, requestDto);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, response));
    }

    // 리뷰 조회 API
    @GetMapping("/{mealItemId}")
    public ResponseEntity<CustomResponse<MealItemReviewResponseDto>> getMealItemReviews(
            @PathVariable Long mealItemId) {
        MealItemReviewResponseDto response = reviewService.getReviewsByMealItemId(mealItemId);
        return ResponseEntity.ok(CustomResponse.onSuccess(GeneralSuccessCode.OK, response));
    }
}
