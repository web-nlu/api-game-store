package vn.edu.hcmaf.apigamestore.category.gameEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryRepository;
import vn.edu.hcmaf.apigamestore.category.CategoryService;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.AddGameRequestDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.product.AccountService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;


    public GameResponseDto toGameResponseDto(GameEntity gameEntity, boolean includeAccounts) {
        GameResponseDto gameResponseDto = GameResponseDto.builder()
                .id(gameEntity.getId())
                .name(gameEntity.getName())
                .categoryId(gameEntity.getCategory().getId())
                .accounts(new ArrayList<>())
                .build();
        if (includeAccounts) {
            gameEntity.getAccounts().forEach(account -> {
                gameResponseDto.getAccounts().add(accountService.toDto(account));
            });
        }
        return gameResponseDto;
    }

    public List<GameEntity> getAllGames() {
        return gameRepository.findByIsDeletedFalse();
    }

    public List<GameEntity> getGamesByCategoryId(long categoryId) {
        return gameRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }

    public GameEntity getGameById(long gameId) {
        GameEntity gameEntity = gameRepository.findByIdAndIsDeletedFalse(gameId);
        if (gameEntity == null) {
            throw new IllegalArgumentException("Game not found with id: " + gameId);
        }
        return gameEntity;
    }

    public GameEntity addGame(AddGameRequestDto gameRequestDto) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setName(gameRequestDto.getName());
        // check category
        CategoryEntity categoryEntity = categoryRepository.findByIdAndIsDeletedFalse(gameRequestDto.getCategoryId());
        if (categoryEntity == null) {
            throw new IllegalArgumentException("Category not found with id: " + gameRequestDto.getCategoryId());
        }
        gameEntity.setCategory(categoryEntity);
        return gameRepository.save(gameEntity);
    }

    public GameEntity updateGame(AddGameRequestDto gameRequestDto, long gameId) {
        // Check if the game exists before updating
        if (!gameRepository.existsByIdAndIsDeletedFalse(gameId)) {
            throw new IllegalArgumentException("Game not found with id: " + gameId);
        }
        GameEntity existingGame = gameRepository.findByIdAndIsDeletedFalse(gameId);
        existingGame.setName(gameRequestDto.getName());
        // check category
        CategoryEntity categoryEntity = categoryRepository.findByIdAndIsDeletedFalse(gameRequestDto.getCategoryId());
        if (categoryEntity == null) {
            throw new IllegalArgumentException("Category not found with id: " + gameRequestDto.getCategoryId());
        }
        existingGame.setCategory(categoryEntity);
        return gameRepository.save(existingGame);


    }

    public void deleteGame(long gameId) {
        GameEntity gameEntity = gameRepository.findByIdAndIsDeletedFalse(gameId);
        if (gameEntity != null) {
            gameEntity.setDeleted(true);
            gameEntity.setDeletedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            gameEntity.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            gameRepository.save(gameEntity);
        } else {
            throw new IllegalArgumentException("Game not found with id: " + gameId);
        }
    }


}
