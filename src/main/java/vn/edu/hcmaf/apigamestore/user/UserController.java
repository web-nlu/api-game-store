package vn.edu.hcmaf.apigamestore.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;

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
        return userService.getCurrentUser();
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/all-lazyloading")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getAllUsersLazyLoading(@RequestBody @Valid LazyLoadingRequestDto<Object> lazyLoadingRequestDto) {
        return userService.getAllUsersLazyLoading(lazyLoadingRequestDto);
    }
    @GetMapping("/id/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody @Valid  UpdateUserDto updateUserDto, @PathVariable Long userId) {
        System.out.println("updateUserDto = " + updateUserDto);
        return userService.updateUser(updateUserDto, userId);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long userId) {
        System.out.println("deleteUser = " + userId);
        return userService.deleteUser(userId);
    }


}
