package vn.edu.hcmaf.apigamestore.product.controller.publicResource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameRepository;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/u")
@RequiredArgsConstructor
@Validated
public class AccountPublicController {

    private final AccountService accountService;
    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getAccountDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account Id : " + id + " success", accountService.getAccountDetail(id)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<BaseResponse> getAccountsByCategory(@PathVariable String categoryId) {
        List<AccountDto> accounts = accountService.getAccountsByCategory(categoryId).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account by Category Id : " + categoryId + " success", accounts));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<BaseResponse> getAccountsByGame(@PathVariable Long gameId) {
        GameEntity game = gameService.getGameById(gameId);
        // Nếu tồn tại, lấy danh sách tài khoản theo gameId
        List<AccountDto> accounts = accountService.getAccountsByGame(game)
                .stream().map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account by Game Id : " + gameId + " success", accounts));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<BaseResponse> searchAccounts(@PathVariable String keyword) {
        List<AccountDto> accounts = accountService.searchAccounts(keyword).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " success", accounts));
    }

    @GetMapping("/search/{keyword}/category/{categoryId}")
    public ResponseEntity<BaseResponse> searchAccountsByCategory(@PathVariable String keyword, @PathVariable String categoryId) {
        List<AccountDto> accounts = accountService.searchAccountsByCategory(keyword, categoryId).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " and Category Id : " + categoryId + " success", accounts));
    }

    @GetMapping("/search/{keyword}/game/{gameId}")
    public ResponseEntity<BaseResponse> searchAccountsByGame(@PathVariable String keyword, @PathVariable String gameId) {
        List<AccountDto> accounts = accountService.searchAccountsByGame(keyword, gameId).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " and Game Id : " + gameId + " success", accounts));
    }

    @PostMapping("/filter")
    public ResponseEntity<BaseResponse> filterAccounts(@RequestBody @Valid AccountFilterRequestDto request) {
        List<AccountDto> accounts = accountService.filterAccounts(request).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Filter accounts success", accounts)
        );
    }

    @GetMapping("/filter-lazyloading")
    public ResponseEntity<BaseResponse> filterAccountsLazyLoading(@ModelAttribute @Valid AccountFilterRequestDto request) {
        Page<AccountDto> accounts = accountService.filterAccountsLazyLoading(request)
                .map(accountService::toDto);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Filter accounts success", accounts)
        );
    }
}