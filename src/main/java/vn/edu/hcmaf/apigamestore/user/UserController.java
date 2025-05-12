package vn.edu.hcmaf.apigamestore.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
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
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getCurrentUser() {
        UserEntity userEntity = userService.getCurrentUser();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User info success", userEntity));
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success", users));
    }
    @PostMapping("/all-lazyloading")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getAllUsersLazyLoading(@RequestBody @Valid LazyLoadingRequestDto<Sort> lazyLoadingRequestDto) {
      LazyLoadingResponseDto<List<UserEntity>> users = userService.getAllUsersLazyLoading(lazyLoadingRequestDto);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success (lazyloading)", users));
    }
    @GetMapping("/id/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long userId) {
      UserEntity userEntity = userService.getUserById(userId);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Id : "+ userId+" info success", userEntity));
    }
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getUserByUsername(@PathVariable String username) {
      UserEntity userEntity = userService.getUserByEmail(username);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Name : "+ username+" info success", userEntity));
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
