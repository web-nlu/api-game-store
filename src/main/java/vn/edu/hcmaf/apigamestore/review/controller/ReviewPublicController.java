package vn.edu.hcmaf.apigamestore.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.review.dto.FilterReviewRequestDTO;
import vn.edu.hcmaf.apigamestore.review.ReviewService;

@RestController
@RequestMapping("/api/reviews/u")
@RequiredArgsConstructor
public class ReviewPublicController {
    private final ReviewService reviewService;

    @GetMapping("/get-reviews/{accountId}")
    public ResponseEntity<BaseResponse> getReviews(@ModelAttribute() @Valid FilterReviewRequestDTO filterReviewRequestDTO, @PathVariable Long accountId) {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Lấy thành công", reviewService.filter(filterReviewRequestDTO, accountId))
        );
    }

    @GetMapping("/get-reviews-by-id/{id}")
    public ResponseEntity<BaseResponse> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Lấy thành công", reviewService.getReviewById(id))
        );
    }
}
