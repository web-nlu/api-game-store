package vn.edu.hcmaf.apigamestore.category.gameEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.AddGameRequestDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
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
        List<GameResponseDto> gameResponseDtos = games.stream()
                .map((gameEntity -> gameEntity.toGameResponseDto(false)))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all games success", gameResponseDtos));
    }
    @GetMapping("/{gameId}")
    public ResponseEntity<BaseResponse> getGameById(@PathVariable long gameId) {
        GameEntity game = gameService.getGameById(gameId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get game by id success", game.toGameResponseDto(true)));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addGame(@RequestBody AddGameRequestDto gameRequestDto) {
        GameEntity newGame = gameService.addGame(gameRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add game success", newGame));
    }
    @PutMapping("/update/{gameId}")
    public ResponseEntity<BaseResponse> updateGame(@RequestBody AddGameRequestDto gameRequestDto, @RequestParam long gameId) {
        GameEntity updatedGame = gameService.updateGame(gameRequestDto, gameId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update game success", updatedGame));
    }
    @DeleteMapping("/delete/{gameId}")
    public ResponseEntity<BaseResponse> deleteGame(@PathVariable String gameId) {
        gameService.deleteGame(Long.parseLong(gameId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete game success", null));
    }


}
