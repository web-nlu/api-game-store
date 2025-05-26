package vn.edu.hcmaf.apigamestore.product.controller.adminController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;

@RestController
@RequestMapping("/api/admin/accounts")
@Validated
/**
 * This controller handles administrative operations related to accounts.
 * It provides endpoints for adding, updating, and deleting accounts.
 */
public class AdminAccountController {
    @Autowired
    private  AccountService accountService;

    /**
     * Adds a new account based on the provided account details.
     *
     * @param dto The account details to be added.
     * @return ResponseEntity containing the ID of the newly created account.
     */
    @Operation(summary = "Add Account", description = "Adds a new account based on the provided account details")
    @PostMapping
    public ResponseEntity<?> addAccount(@RequestBody @Valid AccountDetailDto dto) {
        AccountEntity saved = accountService.addAccount(dto);
        return ResponseEntity.ok(saved.getId()); // hoặc trả dto lại nếu muốn
    }
    /**
     * Updates an existing account.
     * The logic for updating the account should be implemented in the service layer.
     */
    @Operation(summary = "Update Account", description = "Updates an existing account")
    @PutMapping("/update-account")
    public void updateAccount() {
        // Logic to update an account
        // You can call the accountService to perform the necessary operations
        // Example: accountService.updateAccount(existingAccount);
    }
    /**
     * Deletes an account by its ID.
     *
     * @param accountId The ID of the account to be deleted.
     */
    @Operation(summary = "Delete Account", description = "Deletes an account by its ID")
    @DeleteMapping("/delete-account/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        // Logic to delete an account
        // You can call the accountService to perform the necessary operations
        // Example: accountService.deleteAccount(accountId);
    }



}
