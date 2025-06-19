package vn.edu.hcmaf.apigamestore.category.gameEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    List<GameEntity> findByCategoryId(long categoryId);

    GameEntity findByIdAndIsDeletedFalse(long gameId);

    List<GameEntity> findByCategoryIdAndIsDeletedFalse(long categoryId);

    List<GameEntity> findByIsDeletedFalse();

    List<GameEntity> findByIsDeletedFalseOrderByUpdatedAtDesc(Pageable pageable);
    Optional<GameEntity> findByName(String game);
}