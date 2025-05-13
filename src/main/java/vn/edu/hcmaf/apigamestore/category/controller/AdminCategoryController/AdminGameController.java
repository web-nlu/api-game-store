package vn.edu.hcmaf.apigamestore.category.controller.AdminCategoryController;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.AddGameRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

@RestController
@RequestMapping("/api/admin/game")
@RequiredArgsConstructor
public class AdminGameController {
    @Autowired
    private final GameService gameService;
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
