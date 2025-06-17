package vn.edu.hcmaf.apigamestore.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryRepository;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameRepository;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.order.OrderConstants;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;
import vn.edu.hcmaf.apigamestore.review.ReviewEntity;
import vn.edu.hcmaf.apigamestore.review.ReviewService;
import vn.edu.hcmaf.apigamestore.review.dto.ReviewDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewService reviewService;

    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccountDetailDto getAccountDetail(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return toDetailDto(account);
    }

    public AccountDto toDto(AccountEntity entity) {
        return AccountDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .salePrice(entity.getSalePrice() > entity.getPrice() ? 0 : entity.getSalePrice())
                .categoryId(entity.getGame().getCategory().getId())
                .category(entity.getGame().getCategory().getName())
                .gameId(entity.getGame().getId())
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
                .salePrice(entity.getSalePrice() > entity.getPrice() ? 0 : entity.getSalePrice())
                .category(entity.getGame().getCategory().getName())
                .image(entity.getImage())
                .info(entity.getInfo())
                .game(entity.getGame().getName())
                .categoryId(entity.getGame().getCategory().getId())
                .gameId(entity.getGame().getId())
                .server(entity.getServer())
                .description(entity.getDescription())
                .features(entity.getFeatures())
                .level(entity.getLevel())
                .status(entity.getStatus())
                .warranty(entity.getWarranty())
                .createdAt(entity.getCreatedAt().getTime() / 1000)
                .updatedAt(entity.getUpdatedAt().getTime() / 1000)
                .viewCount(entity.getViewCount())
                .saleCount(entity.getSaleCount())
                .tags(entity.getTags())
                .rating(entity.getRating())
                .reviews(entity.getReviews().stream().map(reviewService::toDto).collect(Collectors.toList()))
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

    public AccountEntity findByIdAndIsDeletedFalseAndStatusEquals(Long accountId, String available) {
        return accountRepository.findByIdAndIsDeletedFalseAndStatusEquals(accountId, available)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Page<AccountEntity> filterAccountsLazyLoading(AccountFilterRequestDto request) {
        return accountRepository.filterAccountsLazyLoading(request);
    }

    public AccountEntity findByIdAndIsDeletedFalse(Long accountId) {
        return accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public AccountEntity findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public AccountEntity addAccount(AccountDetailDto dto) {
        AccountEntity account = new AccountEntity();
        account.setTitle(dto.getTitle());
        account.setPrice(dto.getPrice());
        account.setSalePrice(dto.getSalePrice());
        account.setImage(dto.getImage());
        account.setInfo(dto.getInfo());
        account.setServer(dto.getServer());
        account.setDescription(dto.getDescription());
        account.setFeatures(dto.getFeatures());
        account.setLevel(dto.getLevel());
        account.setWarranty(dto.getWarranty());
        account.setTags(dto.getTags());
//        account.setImageGallery(dto.getImageGallery());
        account.setStatus("available");
        account.setViewCount(dto.getViewCount() != null ? dto.getViewCount() : 0);
        account.setSaleCount(dto.getSaleCount() != null ? dto.getSaleCount() : 0);
        account.setRating(dto.getRating() != null ? dto.getRating() : 0.0);

        GameEntity game = gameRepository.findById(dto.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + dto.getGameId()));
        account.setGame(game);
        return accountRepository.save(account);
    }

    public AccountEntity updateAccount(Long id, AccountDetailDto dto) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setTitle(dto.getTitle());
        account.setPrice(dto.getPrice());
        account.setSalePrice(dto.getSalePrice());
        account.setImage(dto.getImage());
        account.setInfo(dto.getInfo());
        account.setServer(dto.getServer());
        account.setDescription(dto.getDescription());
        account.setFeatures(dto.getFeatures());
        account.setLevel(dto.getLevel());
        account.setWarranty(dto.getWarranty());
        account.setTags(dto.getTags());
        if (!account.getGame().getId().equals(dto.getGameId())) {
            GameEntity game = gameRepository.findById(dto.getGameId())
                    .orElseThrow(() -> new IllegalArgumentException("Game not found: " + dto.getGameId()));
            account.setGame(game);
            return accountRepository.save(account);
        }
        return accountRepository.save(account);
    }

    public void updateAccountStatus(Long id, String status) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setStatus(status);
        account.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        accountRepository.save(account);
    }

    public void delete(Long accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setDeleted(true);
        accountRepository.save(account);
    }

    public List<AccountDto> getTop5Accounts() {
        return accountRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<AccountDto> getTopAccountAllGames() {
        return accountRepository.getTop5AccountsByGame().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}