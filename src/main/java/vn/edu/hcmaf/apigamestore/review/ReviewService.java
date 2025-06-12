package vn.edu.hcmaf.apigamestore.review;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.review.dto.CreateReviewDto;
import vn.edu.hcmaf.apigamestore.review.dto.ReviewDto;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewDto toDto(ReviewEntity reviewEntity) {
        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .username(reviewEntity.getUser().getEmail())
                .comment(reviewEntity.getComment())
                .stars(reviewEntity.getStars())
                .OldReviewId(reviewEntity.getOldReviewId())
                .build();
    }

    public ReviewEntity createReview(CreateReviewDto review) {
        try {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setComment(review.getComment());
            reviewEntity.setStars(review.getStars());
            reviewEntity.setAccount(AccountEntity.builder().id(review.getAccountId()).build());
            reviewEntity.setOrder(OrderEntity.builder().id(review.getOrderId()).build());
            reviewEntity.setUser(UserEntity.builder().id(review.getUserId()).build());
            return reviewRepository.save(reviewEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating review (account, order, user not found) : " + e.getMessage());
        }
    }

    @Transactional
    public ReviewEntity updateReview(Long reviewId, CreateReviewDto review) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewEntity.setIsHide(true);
        reviewRepository.save(reviewEntity);
        ReviewEntity newReviewChange = createReview(review);
        newReviewChange.setOldReviewId(reviewEntity.getId());
        return reviewRepository.save(newReviewChange);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }


    public List<ReviewDto> getReviewsByAccountId(Long accountId) {
        return reviewRepository.findByAccountId(accountId).stream()
                .filter(reviewEntity -> !reviewEntity.getIsHide())
                .map(this::toDto)
                .toList();
    }

    public ReviewDto getReviewById(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return toDto(reviewEntity);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


    public void hideReview(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewEntity.setIsHide(true);
        reviewRepository.save(reviewEntity);
    }
}