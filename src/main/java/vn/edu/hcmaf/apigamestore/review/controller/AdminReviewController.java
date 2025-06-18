package vn.edu.hcmaf.apigamestore.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.review.ReviewService;
import vn.edu.hcmaf.apigamestore.review.dto.SetHideRequestDTO;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {
    private final ReviewService reviewService;

    @PutMapping("/set-hide/{reviewId}")
    public ResponseEntity<BaseResponse> hideReview(@PathVariable Long reviewId, @RequestBody SetHideRequestDTO request) {
        reviewService.hideReview(reviewId, request.isHidden());
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Review with ID: " + reviewId + " has been hidden successfully.", null)
        );
    }
    @PutMapping("/delete/{reviewId}")
    public ResponseEntity<BaseResponse> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Review with ID: " + reviewId + " has been deleted successfully.", null)
        );
    }
}
