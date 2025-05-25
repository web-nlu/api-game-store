package vn.edu.hcmaf.apigamestore.category.controller.AdminCategoryController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.AddGameRequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

@RestController
@RequestMapping("/api/admin/game")
@RequiredArgsConstructor
/**
 * AdminGameController handles administrative operations related to games.
 * It provides endpoints for adding, updating, and deleting games.
 */
public class AdminGameController {
    private final GameService gameService;
    /**
     * Retrieves all games.
     * @return A response entity containing a list of all games.
     */
    @Operation(summary = "Get all games", description = "Retrieve all games")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addGame(@RequestBody AddGameRequestDto gameRequestDto) {
        GameEntity newGame = gameService.addGame(gameRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add game success", newGame));
    }
    /**
     * Updates an existing game by its ID.
     * @param gameRequestDto The request body containing the updated game details.
     * @param gameId The ID of the game to be updated.
     * @return A response entity indicating the success of the update operation.
     */
    @Operation(summary = "Update game", description = "Update an existing game by ID")
    @PutMapping("/update/{gameId}")
    public ResponseEntity<BaseResponse> updateGame(@RequestBody AddGameRequestDto gameRequestDto, @RequestParam long gameId) {
        GameEntity updatedGame = gameService.updateGame(gameRequestDto, gameId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update game success", updatedGame));
    }
    /**
     * Deletes a game by its ID.
     * @param gameId The ID of the game to be deleted.
     * @return A response entity indicating the success of the delete operation.
     */
    @Operation(summary = "Delete game", description = "Delete a game by ID")
    @DeleteMapping("/delete/{gameId}")
    public ResponseEntity<BaseResponse> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(Long.parseLong(gameId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete game success", null));
    }

}
