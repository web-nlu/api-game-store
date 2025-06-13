package vn.edu.hcmaf.apigamestore.review;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.review.dto.CreateReviewDto;
import vn.edu.hcmaf.apigamestore.review.dto.FilterReviewRequestDTO;
import vn.edu.hcmaf.apigamestore.review.dto.ReviewDto;
import vn.edu.hcmaf.apigamestore.review.dto.UpdateReviewDTO;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewDto toDto(ReviewEntity reviewEntity) {
        return ReviewDto.builder()
                .id(reviewEntity.getId())
                .email(reviewEntity.getUser().getEmail())
                .comment(reviewEntity.getComment())
                .rating(reviewEntity.getRating())
                .createdAt(reviewEntity.getCreatedAt().getTime() / 1000)
                .build();
    }

    public ReviewEntity createReview(CreateReviewDto review, UserEntity user) {
        try {
            ReviewEntity reviewEntity = new ReviewEntity();
            reviewEntity.setComment(review.getComment());
            reviewEntity.setRating(review.getRating());
            reviewEntity.setAccount(AccountEntity.builder().id(review.getAccountId()).build());
            reviewEntity.setUser(user);
            return reviewRepository.save(reviewEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating review (account, order, user not found) : " + e.getMessage());
        }
    }

    @Transactional
    public ReviewEntity updateReview(Long reviewId, UpdateReviewDTO review, UserEntity user) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá đã bị xoá hoặc không tồn tại"));
        if(!user.getId().equals(reviewEntity.getUser().getId())) {
          throw new IllegalArgumentException("Không có quyền cập nhật đánh giá này này");
        }
        reviewEntity.setComment(review.getComment());
        return reviewRepository.save(reviewEntity);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public LazyLoadingResponseDto<List<ReviewDto>> filter(FilterReviewRequestDTO filter, Long accountId) {
      int limit = filter.getSize() == 0? 10 : filter.getSize() ;
      int offset = (filter.getPage() - 1) * limit;
      LazyLoadingResponseDto<List<ReviewDto>> responseDto = new LazyLoadingResponseDto<>();
      responseDto.setData(reviewRepository.filter(
              filter.getRating(),
              accountId,
              filter.getIsHide(),
              offset,
              limit
      ).stream().map(this::toDto).toList());

      long total = count(filter, accountId);
      responseDto.setTotalElements(total);
      responseDto.setTotalPages((int) Math.ceil((double) total /limit));
      responseDto.setPage(filter.getPage());
      responseDto.setSize(filter.getSize());
      return responseDto;
    }

    public long count(FilterReviewRequestDTO filter, Long accountId) {
      return reviewRepository.count(filter.getRating(),
              accountId,
              filter.getIsHide());
    }


    public List<ReviewDto> getReviewsByAccountId(Long accountId) {
        return reviewRepository.findByAccountId(accountId).stream()
                .filter(ReviewEntity::isHide)
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
        reviewEntity.setHide(true);
        reviewRepository.save(reviewEntity);
    }
}