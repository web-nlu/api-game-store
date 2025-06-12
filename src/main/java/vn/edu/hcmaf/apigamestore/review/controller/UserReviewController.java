package vn.edu.hcmaf.apigamestore.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.review.ReviewEntity;
import vn.edu.hcmaf.apigamestore.review.ReviewService;
import vn.edu.hcmaf.apigamestore.review.dto.CreateReviewDto;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
public class UserReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create-review")
    public ResponseEntity<BaseResponse> createReview(@RequestBody @Valid CreateReviewDto review) {
        ReviewEntity reviewEntity =  reviewService.createReview(review);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Tạo đánh giá thành công", reviewService.toDto(reviewEntity) )
        );
    }
    @PutMapping("/update-review/{reviewId}")
    public ResponseEntity<BaseResponse> updateReview(@PathVariable Long reviewId, @RequestBody @Valid CreateReviewDto review) {
        ReviewEntity reviewEntity = reviewService.updateReview(reviewId, review);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Cập nhật đánh giá thành công", reviewService.toDto(reviewEntity))
        );
    }

}
