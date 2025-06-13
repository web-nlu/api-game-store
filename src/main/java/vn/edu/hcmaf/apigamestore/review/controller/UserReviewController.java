package vn.edu.hcmaf.apigamestore.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.review.ReviewEntity;
import vn.edu.hcmaf.apigamestore.review.ReviewService;
import vn.edu.hcmaf.apigamestore.review.dto.CreateReviewDto;
import vn.edu.hcmaf.apigamestore.review.dto.UpdateReviewDTO;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
public class UserReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<BaseResponse> createReview(@RequestBody @Valid CreateReviewDto review) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserEntity user = userService.getUserByEmail(authentication.getName());
        ReviewEntity reviewEntity =  reviewService.createReview(review, user);
        if(!orderService.checkHaveOrder(user, review.getAccountId())) {
          throw new IllegalArgumentException("Bạn chưa mua sản phẩm!");
        }
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Tạo đánh giá thành công", reviewService.toDto(reviewEntity) )
        );
    }
    @PutMapping("/{reviewId}")
    public ResponseEntity<BaseResponse> updateReview(@PathVariable Long reviewId, @RequestBody @Valid UpdateReviewDTO review) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserEntity user = userService.getUserByEmail(authentication.getName());
        ReviewEntity reviewEntity = reviewService.updateReview(reviewId, review, user);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Cập nhật đánh giá thành công", reviewService.toDto(reviewEntity))
        );
    }

}
