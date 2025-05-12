package vn.edu.hcmaf.apigamestore.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameRepository;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final GameRepository gameRepository;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailDto> getAccountDetail(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountDetail(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse> getAccountsByCategory(@PathVariable String categoryId) {
        List<AccountDto> accounts = accountService.getAccountsByCategory(categoryId).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get Account by Category Id : " + categoryId + " success", accounts));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<BaseResponse> getAccountsByGame(@PathVariable String gameId) {
        GameEntity game = gameRepository.findById(Long.valueOf(gameId))
                .orElseThrow(() -> new RuntimeException("Game not found"));


        // Nếu tồn tại, lấy danh sách tài khoản theo gameId
        List<AccountDto> accounts = accountService.getAccountsByGame(game).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get Account by Game Id : " + gameId + " success", accounts));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<BaseResponse> searchAccounts(@PathVariable String keyword) {
        List<AccountDto> accounts = accountService.searchAccounts(keyword).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " success", accounts));
    }

    @GetMapping("/search/{keyword}/category/{categoryId}")
    public ResponseEntity<BaseResponse> searchAccountsByCategory(@PathVariable String keyword, @PathVariable String categoryId) {
        List<AccountDto> accounts = accountService.searchAccountsByCategory(keyword, categoryId).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " and Category Id : " + categoryId + " success", accounts));
    }

    @GetMapping("/search/{keyword}/game/{gameId}")
    public ResponseEntity<BaseResponse> searchAccountsByGame(@PathVariable String keyword, @PathVariable String gameId) {
        List<AccountDto> accounts = accountService.searchAccountsByGame(keyword, gameId).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " and Game Id : " + gameId + " success", accounts));
    }
    @PostMapping("/accounts/filter")
    public ResponseEntity<BaseResponse> filterAccounts(@RequestBody AccountFilterRequestDto request) {
        List<AccountDto> accounts = accountService.filterAccounts(request).stream()
                .map(account -> AccountDto.builder()
                        .id(account.getId())
                        .title(account.getTitle())
                        .price(account.getPrice())
                        .category(account.getGame().getCategory().getName())
                        .image(account.getImage())
                        .info(account.getInfo())
                        .game(account.getGame().getName())
                        .build())
                .toList();

        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Filter accounts success", accounts)
        );
    }
}