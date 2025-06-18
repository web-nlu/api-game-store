package vn.edu.hcmaf.apigamestore.category.controller.AdminCategoryController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.SetGameRequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/admin/game")
@RequiredArgsConstructor
/**
 * AdminGameController handles administrative operations related to games.
 * It provides endpoints for adding, updating, and deleting games.
 */
public class AdminGameController {
    private final GameService gameService;

  @PutMapping("/update/{categoryId}")
  public ResponseEntity<BaseResponse> setGame(@RequestBody List<SetGameRequestDto> gameRequestDto, @PathVariable long categoryId) {
    List<GameResponseDto> games = gameService.updateGame(gameRequestDto, categoryId);
    return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update game success", games));
  }
    /**
     * Deletes a game by its ID.
     * @param gameId The ID of the game to be deleted.
     * @return A response entity indicating the success of the delete operation.
     */
    @Operation(summary = "Delete game", description = "Delete a game by ID")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<BaseResponse> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(Long.parseLong(gameId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete game success", null));
    }

}
