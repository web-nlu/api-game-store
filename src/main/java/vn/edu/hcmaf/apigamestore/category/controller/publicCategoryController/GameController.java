package vn.edu.hcmaf.apigamestore.category.controller.publicCategoryController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/game/u")
@RequiredArgsConstructor
/**
 * GameController handles public operations related to games.
 * It provides endpoints for retrieving all games, getting a game by its ID,
 * and getting games by category.
 */
public class GameController {
    private final GameService gameService;

    /**
     * Retrieves all games.
     *
     * @return A response entity containing a list of all games.
     */
    @Operation(summary = "Get all games", description = "Retrieve all games")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllGames() {
        List<GameResponseDto> gameResponseDtos = gameService.getAllGames().stream()
                .map(gameEntity -> gameService.toGameResponseDto(gameEntity, false))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all games success", gameResponseDtos));
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param gameId The ID of the game to be retrieved.
     * @return A response entity containing the game details.
     */
    @Operation(summary = "Get game by ID", description = "Retrieve a game by its ID")
    @GetMapping("/{gameId}")
    public ResponseEntity<BaseResponse> getGameById(@PathVariable long gameId) {
        GameEntity game = gameService.getGameById(gameId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get game by id success", gameService.toGameResponseDto(game, true)));
    }

    /**
     * Retrieves games by category ID.
     *
     * @param categoryId The ID of the category to filter games by.
     * @return A response entity containing a list of games in the specified category.
     */
    @Operation(summary = "Get games by category", description = "Retrieve games by category ID")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse> getGamesByCategory(@PathVariable long categoryId) {
        List<GameResponseDto> gameResponseDtos = gameService.getGamesByCategory(categoryId).stream()
                .map(gameEntity -> gameService.toGameResponseDto(gameEntity, false))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get games by category success", gameResponseDtos));
    }


}
