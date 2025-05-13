package vn.edu.hcmaf.apigamestore.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;
import vn.edu.hcmaf.apigamestore.review.ReviewEntity;
import vn.edu.hcmaf.apigamestore.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public AccountEntity findByIdAndIsDeletedFalseAndStatusEquals(Long id, String status) {
        return accountRepository.findByIdAndIsDeletedFalseAndStatusEquals(id, status);
    }
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccountDetailDto getAccountDetail(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return toDetailDto(account);
    }

    public AccountDto toDto(AccountEntity entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .category(entity.getGame().getCategory().getName())
                .image(entity.getImage())
                .info(entity.getInfo())
                .game(entity.getGame().getName())
                .build();
    }

    public AccountDetailDto toDetailDto(AccountEntity entity) {
        return AccountDetailDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .salePrice(entity.getSalePrice())
                .category(entity.getGame().getCategory().getName())
                .image(entity.getImage())
                .info(entity.getInfo())
                .game(entity.getGame().getName())
                .server(entity.getServer())
                .imageGallery(entity.getImageGallery())
                .description(entity.getDescription())
                .features(entity.getFeatures())
                .level(entity.getLevel())
                .status(entity.getStatus())
                .warranty(entity.getWarranty())
                .createdAt(LocalDateTime.parse(entity.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(entity.getUpdatedAt()))
                .viewCount(entity.getViewCount())
                .saleCount(entity.getSaleCount())
                .tags(entity.getTags())
                .rating(entity.getRating())
                .reviews(entity.getReviews().stream().map(this::toReviewDto).collect(Collectors.toList()))
                .build();
    }

    private ReviewDto toReviewDto(ReviewEntity review) {
        return ReviewDto.builder()
                .username(review.getUsername())
                .comment(review.getComment())
                .stars(review.getStars())
                .build();
    }

    public List<AccountEntity> getAccountsByCategory(String categoryId) {
        return accountRepository.findByCategoryId(categoryId);
    }

    public List<AccountEntity> getAccountsByGame(GameEntity gameEntity) {
        return accountRepository.findByGame(gameEntity);
    }

    public List<AccountEntity> searchAccounts(String keyword) {
        return accountRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<AccountEntity> searchAccountsByCategory(String keyword, String categoryId) {
         return accountRepository.findByTitleContainingIgnoreCaseAndCategoryId(keyword, categoryId);
    }

    public List<AccountEntity> searchAccountsByGame(String keyword, String gameId) {
        return accountRepository.findByTitleContainingIgnoreCaseAndGameId(keyword, gameId);
    }

    public List<AccountEntity>  searchAccountsByCategoryAndGame(String keyword, String categoryId, String gameId, String sort) {
        return accountRepository.findByTitleContainingIgnoreCaseAndCategoryId(keyword, categoryId);
    }

    public List<AccountEntity> filterAccounts(AccountFilterRequestDto request) {
        return accountRepository.filterAccounts(request);
    }
}