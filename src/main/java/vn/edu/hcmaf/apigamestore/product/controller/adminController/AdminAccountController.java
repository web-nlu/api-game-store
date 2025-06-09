package vn.edu.hcmaf.apigamestore.product.controller.adminController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmaf.apigamestore.common.constants.EntityConstant;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.images.ImagesEntity;
import vn.edu.hcmaf.apigamestore.images.ImagesService;
import vn.edu.hcmaf.apigamestore.images.dto.ImagesDTO;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDetailDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@Validated
@RequiredArgsConstructor
/**
 * This controller handles administrative operations related to accounts.
 * It provides endpoints for adding, updating, and deleting accounts.
 */
public class AdminAccountController {
    private final AccountService accountService;
    private final ImagesService imagesService;
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
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS","Tạo thành công", accountService.toDto(saved))
        );
    }
    /**
     * Updates an existing account.
     * The logic for updating the account should be implemented in the service layer.
     */
    @Operation(summary = "Update Account", description = "Updates an existing account")
    @PutMapping("/update-account/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody @Valid AccountDetailDto dto ) {
      AccountEntity updated = accountService.updateAccount(id, dto);
      return ResponseEntity.ok().body(
              new SuccessResponse<>("SUCCESS","Cập nhật thành công", accountService.toDto(updated))
      );
    }
    /**
     * Deletes an account by its ID.
     *
     * @param accountId The ID of the account to be deleted.
     */
    @Operation(summary = "Delete Account", description = "Deletes an account by its ID")
    @DeleteMapping("/delete-account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
      accountService.delete(accountId);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Xoá thành công", accountId));
    }

    @PutMapping("/images/{id}")
    public ResponseEntity<?> uploadImages(@PathVariable Long id, @RequestBody List<ImagesDTO> imagesDTO) {
      AccountEntity account = this.accountService.findById(id);
      List<ImagesEntity> images = imagesService.setImages(EntityConstant.ACCOUNT, account.getId(), imagesDTO);

      return ResponseEntity.ok().body(
              new SuccessResponse<>("SUCCESS", "Cập nhật thành công", images)
      );
    }
}
