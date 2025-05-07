package vn.edu.hcmaf.apigamestore.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.common.dto.*;
import vn.edu.hcmaf.apigamestore.role.RoleRepository;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<BaseResponse> getAllUsers() {
        return ResponseEntity.ok().body(new SuccessResponse<List<UserEntity>>("200", "Get all users successfully", userRepository.findAll()));
    }

    public ResponseEntity<BaseResponse> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent() && !userEntity.get().isDeleted()) {
            userEntity.get().setPassword(null);
            return ResponseEntity.ok().body(new SuccessResponse<UserEntity>("200", "Get current user successfully", userEntity.get()));
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("404", "User not found", ""));
        }
    }

    public ResponseEntity<BaseResponse> getAllUsersLazyLoading(LazyLoadingRequestDto lazyLoadingRequestDto) {

        int page = lazyLoadingRequestDto.getPage();
        int size = lazyLoadingRequestDto.getSize();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UserEntity> userEntities = userRepository.findAllByIsDeletedFalse(pageable);
        if (!userEntities.isEmpty()) {
            for (UserEntity user : userEntities) {
                user.setPassword(null);
            }
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "Get all users successfully", LazyLoadingResponseDto.<List<UserEntity>>builder()
                    .page(page)
                    .size(size)
                    .totalElements(userRepository.countByIsDeletedFalse())
                    .totalPages((int) Math.ceil((double) userRepository.countByIsDeletedFalse() / size))
                    .data(userEntities)
                    .build()));
        }
        return ResponseEntity.status(404).body(new ErrorResponse("404", "No users found", ""));
    }

    public ResponseEntity<BaseResponse> getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent() && !userEntity.get().isDeleted()) {
            userEntity.get().setPassword(null);
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "Get user successfully", userEntity.get()));
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("404", "User not found", ""));
        }
    }

    public ResponseEntity<BaseResponse> findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent() && !userEntity.get().isDeleted()) {
            userEntity.get().setPassword(null);
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "Get user successfully", userEntity.get()));
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("404", "User not found", ""));
        }
    }

    public ResponseEntity<BaseResponse> updateUser(UpdateUserDto updateUserDto, Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByIdAndIsDeletedFalse(id);
        if (userEntity.isPresent()
                && (currentUser.equals(userEntity.get().getUsername()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ADMIN"))
        )
        ) {
            UserEntity user = userEntity.get();

            if (updateUserDto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
            }
            if (updateUserDto.getEmail() != null) {
                user.setEmail(updateUserDto.getEmail());
            }
            if (updateUserDto.getPhoneNumber() != null) {
                user.setPhoneNumber(updateUserDto.getPhoneNumber());
            }
            if (updateUserDto.getAddress() != null) {
                user.setAddress(updateUserDto.getAddress());
            }
            if (updateUserDto.getAvatar() != null) {
                user.setAvatar(updateUserDto.getAvatar());
            }
            if (updateUserDto.getRoles() != null) {
                Arrays.stream(updateUserDto.getRoles())
                        .forEach(role -> {
                            if (roleService.existsByName(role)) {
                                user.getRoles().add(roleService.findByName(role).get());
                            }
                        });
            }

            userRepository.save(user);
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "User updated successfully", Map.of("id", user.getId())));
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("404", "User not found", ""));
        }
    }

    public ResponseEntity<BaseResponse> deleteUser(Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByIdAndIsDeletedFalse(id);
        if (userEntity.isPresent()
                && (currentUser.equals(userEntity.get().getUsername()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(
                authority -> authority.getAuthority().equals("ADMIN"))
        )
        ) {
            userEntity.get().setDeleted(true);
            userEntity.get().setDeletedAt(String.valueOf(LocalDateTime.now()));
            userEntity.get().setDeletedBy(currentUser);
            userRepository.save(userEntity.get());
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "User deleted successfully",  Map.of("id", userEntity.get().getId())));
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("404", "User not found", ""));
        }
    }


}
