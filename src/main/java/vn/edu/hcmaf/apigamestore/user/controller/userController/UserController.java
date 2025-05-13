package vn.edu.hcmaf.apigamestore.user.controller.userController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.cart.CartEntity;
import vn.edu.hcmaf.apigamestore.cart.CartService;
import vn.edu.hcmaf.apigamestore.cart.dto.CartResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;
import vn.edu.hcmaf.apigamestore.user.UserService;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getCurrentUser() {
        UserEntity userEntity = userService.getCurrentUser();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User info success", userEntity));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody @Valid  UpdateUserDto updateUserDto, @PathVariable Long userId) {
      UserEntity userEntity = userService.updateUser(updateUserDto, userId);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Update User Id: "+userId+ " success", userEntity));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long userId) {
        System.out.println("deleteUser = " + userId);
        boolean result = userService.deleteUser(userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete User Id: "+userId+ " success",result));
    }

}
