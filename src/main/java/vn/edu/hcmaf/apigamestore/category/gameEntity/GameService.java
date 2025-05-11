package vn.edu.hcmaf.apigamestore.category.gameEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameEntity> getAllGames() {
        return gameRepository.findByIsDeletedFalse();
    }

    public List<GameEntity> getGamesByCategoryId(long categoryId) {
        return gameRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }

    public GameEntity addGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    public GameEntity updateGame(GameEntity gameEntity) {
        return gameRepository.save(gameEntity);
    }

    public void deleteGame(long gameId) {
        // Check if the game exists before deleting
        if (!gameRepository.existsByIdAndIsDeletedFalse(gameId)) {
            throw new RuntimeException("Game not found with id: " + gameId);
        }
        GameEntity gameEntity = gameRepository.findByIdAndIsDeletedFalse(gameId);
        if (gameEntity != null) {
            gameEntity.setDeleted(true);
            gameEntity.setDeletedAt(String.valueOf(java.time.LocalDateTime.now()));
            gameEntity.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            gameRepository.save(gameEntity);
        } else {
            throw new RuntimeException("Game not found with id: " + gameId);
        }
    }
}
