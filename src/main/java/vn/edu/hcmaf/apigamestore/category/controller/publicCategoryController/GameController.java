package vn.edu.hcmaf.apigamestore.category.controller.publicCategoryController;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.AddGameRequestDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/game/u")
@RequiredArgsConstructor
public class GameController {
    @Autowired
    private final GameService gameService;
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllGames() {
        List<GameResponseDto> gameResponseDtos = gameService.getAllGames().stream()
                .map(gameEntity -> gameService.toGameResponseDto(gameEntity, false))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all games success", gameResponseDtos));
    }
    @GetMapping("/{gameId}")
    public ResponseEntity<BaseResponse> getGameById(@PathVariable long gameId) {
        GameEntity game = gameService.getGameById(gameId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get game by id success", gameService.toGameResponseDto(game,true)));
    }




}
