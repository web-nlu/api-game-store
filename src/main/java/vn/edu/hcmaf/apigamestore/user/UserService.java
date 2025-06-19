package vn.edu.hcmaf.apigamestore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.*;
import vn.edu.hcmaf.apigamestore.email.EmailService;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleRepository;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleEntity;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleRepository;
import vn.edu.hcmaf.apigamestore.role.dto.RoleDto;
import vn.edu.hcmaf.apigamestore.user.dto.FilterUserRequestDTO;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;
import vn.edu.hcmaf.apigamestore.user.dto.UserResponseDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public UserResponseDto toUserResponseDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .avatar(userEntity.getAvatar())
                .phoneNumber(userEntity.getPhoneNumber())
                .address(userEntity.getAddress())
                .numOfCartItem(userEntity.getCartItems() == null ? 0 : userEntity.getCartItems().size())
                .activeRoles(userEntity.getActiveRoles())
                .build();
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmailAndIsDeletedFalse(username).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("user with email " + username + " not found");
        }
        userEntity.setPassword(null);
        return userEntity;
    }

    public UserEntity create(RegisterRequestDto requestDto, RoleEntity role) {
      if (userRepository.existsByEmail(requestDto.getEmail())){
        throw new IllegalArgumentException("User with this email already exists");
      }
      UserEntity userEntity = new UserEntity();
      userEntity.setEmail(requestDto.getEmail());
      userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));

      UserRoleEntity userRoleEntity = new UserRoleEntity();
      userRoleEntity.setUser(userEntity);
      userRoleEntity.setRole(role);
      userEntity.setUserRoles(Collections.singletonList(userRoleEntity));
      return userRepository.save(userEntity);
    }

    public LazyLoadingResponseDto<List<UserResponseDto>> filter(FilterUserRequestDTO requestDTO) {
        int page = requestDTO.getPage();
        int size = requestDTO.getSize();
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UserEntity> userEntities = userRepository.filter(requestDTO.getEmail(), requestDTO.getRole(), pageable);
        if (!userEntities.isEmpty()) {
            for (UserEntity user : userEntities) {
                user.setPassword(null);
            }
        }
        return LazyLoadingResponseDto.<List<UserResponseDto>>builder()
                .page(page)
                .size(size)
                .totalElements((long) userEntities.size())
                .totalPages((int) Math.ceil((double) userEntities.size() / size))
                .data(userEntities.stream().map(this::toUserResponseDto).toList())
                .build();
    }

    public UserEntity getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new NullPointerException("user with id " + id + " not found");
        }
        userEntity.setPassword(null);
        return userEntity;
    }

    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        return userEntity;
    }

    public UserEntity updateUser(UpdateUserDto updateUserDto, Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (user == null) {
            throw new NullPointerException("user with id " + id + " not found");
        }

        if (!currentUser.equals(user.getEmail()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().noneMatch(
                authority -> authority.getAuthority().equals("ADMIN"))) {
            throw new IllegalArgumentException("");
        }
        if (updateUserDto.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
//        if (updateUserDto.getAddress() != null) {
//            user.setAddress(updateUserDto.getAddress());
//        }
        if (updateUserDto.getAvatar() != null) {
            user.setAvatar(updateUserDto.getAvatar());
        }
        return userRepository.save(user);
    }
    /**
     * Update the password of a user by their ID.
     * The password is encoded before saving.
     *
     * @param password The new password to set for the user
     * @param id       The ID of the user whose password is to be updated
     * @return Updated UserEntity with the new password
     */
    public UserEntity updatePassUser(String password, Long id) {
        UserEntity userEntity = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("user with id " + id + " not found");
        }
        userEntity.setPassword(passwordEncoder.encode(password));
        // update token logic
        return userRepository.save(userEntity);
    }
    /**
     * Update user roles based on the provided roleUpdateMap.
     * If a role is active (true), it will be added to the user.
     * If a role is inactive (false), it will be softly deleted from the user's roles.
     *
     * roleUpdateMap should contain role IDs as keys and their active status (true/false) as values.
     * true means the role should be added to the user,
     * false means the role should be removed (soft deleted) from the user.
     * if user already has the role, and it is active, do not add it into roleUpdateMap, because it is already active, and this method will not add it again.
     *
     * @param roleUpdateMap Map of role IDs and their active status
     * @param id            User ID
     * @return Updated UserEntity
     */
    public UserEntity updateRolesUser(Map<Long,Boolean> roleUpdateMap, Long id) {
        UserEntity userEntity = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("user with id " + id + " not found");
        }

        List<RoleEntity> allRoles = roleRepository.findAllByIdInAndIsDeletedFalse(roleUpdateMap.keySet());
        Map<Long, RoleEntity> roleMap = allRoles.stream().collect(Collectors.toMap(RoleEntity::getId, r -> r));

        List<UserRoleEntity> existingUserRoles = userRoleRepository.findAllByUserAndIsDeletedFalse(userEntity);
        Map<Long, UserRoleEntity> userRoleMap = existingUserRoles.stream()
                .collect(Collectors.toMap(ur -> ur.getRole().getId(), ur -> ur));


        for (Map.Entry<Long, Boolean> entry : roleUpdateMap.entrySet()) {
            Long roleId = entry.getKey();
            Boolean isActive = entry.getValue();

            RoleEntity roleEntity = roleMap.get(roleId);
            if (roleEntity == null) {
                throw new IllegalArgumentException("Role with id " + roleId + " not found");
            }

            UserRoleEntity userRole = userRoleMap.get(roleId);

            // if the user already has the role, and the role is active, do not add it into roleUpdateMap, because it is already active, and this method will not add it again.
            // if the user does not have the role, and the role is active, add it
            if (userRole == null && isActive) {
                // add new role
                UserRoleEntity newUserRole = new UserRoleEntity();
                newUserRole.setUser(userEntity);
                newUserRole.setRole(roleEntity);
                newUserRole.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                newUserRole.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                newUserRole.setDeleted(false);
                userRoleRepository.save(newUserRole);

            } else if (userRole != null) {
                if (!isActive) {
                    // Soft delete
                    userRole.setDeleted(true);
                    userRole.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                    userRole.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                    userRoleRepository.save(userRole);
                }
            }
        }
        return userEntity;
    }

    public boolean deleteUser(Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        //has ADMIN role or is the user itself
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().noneMatch(
                authority -> authority.getAuthority().equals("ADMIN"))) {
            UserEntity currentUserEntity = userRepository.findByEmail(currentUser).orElse(null);
            if (currentUserEntity == null || !currentUserEntity.getId().equals(id)) {
                throw new IllegalArgumentException("Bạn không có quyền xóa người dùng này");
            }
        }
        UserEntity userEntity = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (userEntity == null) {
            throw new NullPointerException("Không tìm thấy người dùng");
        }
        userEntity.setDeleted(true);
        userEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        userEntity.setDeletedBy(currentUser);
        userRepository.save(userEntity);
        return true;
    }

  /**
   * Reset pass
   *
   * @param userEntity The user entity to reset password for.
   */
  public void resetPassword(UserEntity userEntity) {
    if (userEntity == null) {
      throw new NullPointerException("User cannot be null");
    }
    // Reset password to ramdom value
    String randompass = UUID.randomUUID().toString().substring(0, 8);
    userEntity.setPassword(passwordEncoder.encode(randompass));
    // Send email with new password
    emailService.sendResetPass(userEntity.getEmail(), userEntity.getUsername(), randompass);

    userRepository.save(userEntity);
  }
}
