package vn.edu.hcmaf.apigamestore.product.controller.adminController;

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
public class AdminAccountController {
    @Autowired
    private  AccountService accountService;


    @PostMapping
    public ResponseEntity<?> addAccount(@RequestBody @Valid AccountDetailDto dto) {
        AccountEntity saved = accountService.addAccount(dto);
        return ResponseEntity.ok(saved.getId()); // hoặc trả dto lại nếu muốn
    }
    @PutMapping("/update-account")
    public void updateAccount() {
        // Logic to update an account
        // You can call the accountService to perform the necessary operations
        // Example: accountService.updateAccount(existingAccount);
    }
    @DeleteMapping("/delete-account/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        // Logic to delete an account
        // You can call the accountService to perform the necessary operations
        // Example: accountService.deleteAccount(accountId);
    }



}
