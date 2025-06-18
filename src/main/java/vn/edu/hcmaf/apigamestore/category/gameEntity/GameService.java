package vn.edu.hcmaf.apigamestore.category.gameEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryRepository;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.SetGameRequestDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.product.AccountService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public GameEntity addGame(SetGameRequestDto gameRequestDto, CategoryEntity categoryEntity) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setName(gameRequestDto.getName());
        return gameRepository.save(gameEntity);
    }

    public List<GameResponseDto> updateGame(List<SetGameRequestDto> gameRequestDto, long categoryId) {
        // Check if the game exists before updating
      CategoryEntity category = categoryRepository.findByIdAndIsDeletedFalse(categoryId).orElseThrow(() ->
              new IllegalArgumentException("Category not found with id: " + categoryId));
      List<GameEntity> updatedGames = gameRequestDto.stream().map((dto) -> {
        GameEntity gameEntity = new GameEntity();
        if(dto.getId() != null) {
          gameEntity.setId(dto.getId());
        }
        gameEntity.setName(dto.getName());
        gameEntity.setCategory(category);
        return gameEntity;
      }).toList();
      List<GameEntity> savedGames = gameRepository.saveAll(updatedGames);
      return savedGames.stream().map((entity) -> toGameResponseDto(entity, false)).toList();
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


    public List<GameEntity> getGamesByCategory(long categoryId) {
    return gameRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }
}
