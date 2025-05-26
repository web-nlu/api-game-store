package vn.edu.hcmaf.apigamestore.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;
import vn.edu.hcmaf.apigamestore.product.repo.AccountRepositoryCustom;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, AccountRepositoryCustom {
    // Find an account by its ID and ensure it is not deleted and status is available mean = 1

    List<AccountEntity> findByGame(GameEntity game);

    List<AccountEntity> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT a FROM AccountEntity a JOIN a.game g WHERE g.category.id = :categoryId")
    List<AccountEntity> findByCategoryId(String categoryId);

    @Query("SELECT a FROM AccountEntity a JOIN a.game g WHERE a.title LIKE %:keyword% AND g.category.id = :categoryId")
    List<AccountEntity> findByTitleContainingIgnoreCaseAndCategoryId(@Param("keyword") String keyword, @Param("categoryId") String categoryId);

    @Query("SELECT a FROM AccountEntity a JOIN a.game g WHERE a.title LIKE %:keyword% AND g.id = :gameId")
    List<AccountEntity> findByTitleContainingIgnoreCaseAndGameId(String keyword, String gameId);

    Optional<AccountEntity> findByIdAndIsDeletedFalseAndStatusEquals(Long accountId, String available);

    Optional<AccountEntity> findByIdAndIsDeletedFalse(Long accountId);
}