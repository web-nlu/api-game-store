package vn.edu.hcmaf.apigamestore.category.gameEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllGames() {
        List<GameEntity> games = gameService.getAllGames();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all games success", games));
    }
    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addGame(GameEntity gameEntity) {
        GameEntity newGame = gameService.addGame(gameEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add game success", newGame));
    }
    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateGame(GameEntity gameEntity) {
        GameEntity updatedGame = gameService.updateGame(gameEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update game success", updatedGame));
    }
    @DeleteMapping("/delete/{gameId}")
    public ResponseEntity<BaseResponse> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(Long.parseLong(gameId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete game success", null));
    }


}
