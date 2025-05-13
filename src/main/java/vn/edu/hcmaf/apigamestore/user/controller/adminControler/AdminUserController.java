package vn.edu.hcmaf.apigamestore.user.controller.adminControler;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@Validated
public class AdminUserController {
    @Autowired
    private UserService userService;
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success", users));
    }
    @PostMapping("/all-lazyloading")
    public ResponseEntity<BaseResponse> getAllUsersLazyLoading(@RequestBody @Valid LazyLoadingRequestDto<Sort> lazyLoadingRequestDto) {
        LazyLoadingResponseDto<List<UserEntity>> users = userService.getAllUsersLazyLoading(lazyLoadingRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success (lazyloading)", users));
    }
    @GetMapping("/id/{userId}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Id : "+ userId+" info success", userEntity));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<BaseResponse> getUserByUsername(@PathVariable String username) {
        UserEntity userEntity = userService.getUserByEmail(username);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Name : "+ username+" info success", userEntity));
    }
}
