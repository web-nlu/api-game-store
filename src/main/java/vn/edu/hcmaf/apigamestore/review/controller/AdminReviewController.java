package vn.edu.hcmaf.apigamestore.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.review.ReviewService;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {
    private final ReviewService reviewService;

    @PutMapping("/hide-review/{reviewId}")
    public ResponseEntity<BaseResponse> hideReview(Long reviewId) {
        reviewService.hideReview(reviewId);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Review with ID: " + reviewId + " has been hidden successfully.", null)
        );
    }
    @PutMapping("/delete/{reviewId}")
    public ResponseEntity<BaseResponse> deleteReview(Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Review with ID: " + reviewId + " has been deleted successfully.", null)
        );
    }
}
