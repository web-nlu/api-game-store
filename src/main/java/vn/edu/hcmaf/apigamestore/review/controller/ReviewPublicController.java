package vn.edu.hcmaf.apigamestore.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.review.ReviewService;

@RestController
@RequestMapping("/api/reviews/u")
@RequiredArgsConstructor
public class ReviewPublicController {
    private final ReviewService reviewService;

    @GetMapping("/get-reviews")
    public ResponseEntity<BaseResponse> getReviews() {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Lấy thành công", reviewService.getAllReviews())
        );
    }

    @GetMapping("/get-reviews-by-id/{id}")
    public ResponseEntity<BaseResponse> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Lấy thành công", reviewService.getReviewById(id))
        );
    }
}
