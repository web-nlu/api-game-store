package vn.edu.hcmaf.apigamestore.product.controller.publicResource;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/u")
@RequiredArgsConstructor
@Validated
/**
 * This controller handles public operations related to accounts.
 * It provides endpoints for retrieving account information, filtering accounts,
 * and searching accounts based on various criteria.
 */
public class AccountPublicController {

    private final AccountService accountService;
    private final GameService gameService;
    /**
     * Retrieves a list of all accounts.
     *
     * @return ResponseEntity containing a list of AccountDto objects.
     */
    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
    /**
     * Retrieves account details by account ID.
     *
     * @param id The ID of the account to retrieve.
     * @return ResponseEntity containing the account details.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve account details by account ID")
    public ResponseEntity<BaseResponse> getAccountDetail(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account Id : " + id + " success", accountService.getAccountDetail(id)));
    }
    /**
     * Retrieves accounts associated with a specific category ID.
     *
     * @param categoryId The ID of the category to filter accounts by.
     * @return ResponseEntity containing a list of AccountDto objects associated with the specified category.
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get accounts by category ID", description = "Retrieve accounts associated with a specific category ID")
    public ResponseEntity<BaseResponse> getAccountsByCategory(@PathVariable String categoryId) {
        List<AccountDto> accounts = accountService.getAccountsByCategory(categoryId).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account by Category Id : " + categoryId + " success", accounts));
    }
    /**
     * Retrieves accounts associated with a specific game ID.
     *
     * @param gameId The ID of the game to filter accounts by.
     * @return ResponseEntity containing a list of AccountDto objects associated with the specified game.
     */
    @GetMapping("/game/{gameId}")
    @Operation(summary = "Get accounts by game ID", description = "Retrieve accounts associated with a specific game ID")
    public ResponseEntity<BaseResponse> getAccountsByGame(@PathVariable Long gameId) {
        GameEntity game = gameService.getGameById(gameId);
        // Nếu tồn tại, lấy danh sách tài khoản theo gameId
        List<AccountDto> accounts = accountService.getAccountsByGame(game)
                .stream().map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Account by Game Id : " + gameId + " success", accounts));
    }
    /**
     * Searches for accounts based on a keyword.
     *
     * @param keyword The keyword to search for in account details.
     * @return ResponseEntity containing a list of AccountDto objects that match the search criteria.
     */
    @GetMapping("/search/{keyword}")
    @Operation(summary = "Search accounts by keyword", description = "Search accounts based on a keyword")
    public ResponseEntity<BaseResponse> searchAccounts(@PathVariable String keyword) {
        List<AccountDto> accounts = accountService.searchAccounts(keyword).stream()
                .map(accountService::toDto).toList();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Search Account by keyword : " + keyword + " success", accounts));
    }

    /**
     * Filters accounts based on various criteria provided in the request.
     *
     * @param request The request containing filter criteria (AccountFilterRequestDto).
     * @return ResponseEntity containing a paginated list of AccountDto objects that match the filter criteria.
     */
    @GetMapping("/filter-lazyloading")
    @Operation(summary = "Filter accounts with lazy loading", description = "Filter accounts based on various criteria with lazy loading")
    public ResponseEntity<BaseResponse> filterAccountsLazyLoading(@ModelAttribute @Valid AccountFilterRequestDto request) {
        Page<AccountDto> accounts = accountService.filterAccountsLazyLoading(request)
                .map(accountService::toDto);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Filter accounts success", accounts)
        );
    }
}