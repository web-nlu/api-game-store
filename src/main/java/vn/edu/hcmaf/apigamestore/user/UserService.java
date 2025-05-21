package vn.edu.hcmaf.apigamestore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.common.dto.*;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleRepository;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleEntity;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleRepository;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(username).orElse(null);
        if (userEntity == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại");
        }
        userEntity.setPassword(null);
        return userEntity;
    }

    public LazyLoadingResponseDto<List<UserEntity>> getAllUsersLazyLoading(LazyLoadingRequestDto lazyLoadingRequestDto) {
        int page = lazyLoadingRequestDto.getPage();
        int size = lazyLoadingRequestDto.getSize();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UserEntity> userEntities = userRepository.findAllByIsDeletedFalse(pageable);
        if (!userEntities.isEmpty()) {
            for (UserEntity user : userEntities) {
                user.setPassword(null);
            }
        }
        return LazyLoadingResponseDto.<List<UserEntity>>builder()
                .page(page)
                .size(size)
                .totalElements(userRepository.countByIsDeletedFalse())
                .totalPages((int) Math.ceil((double) userRepository.countByIsDeletedFalse() / size))
                .data(userEntities)
                .build();
    }

    public UserEntity getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            throw new NullPointerException("Không tìm thấy người dùng");
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
            throw new NullPointerException("Không tìm thấy người dùng");
        }

        if (!currentUser.equals(user.getEmail()) || SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().noneMatch(
                authority -> authority.getAuthority().equals("ADMIN"))) {
            throw new IllegalArgumentException("");
        }
        // logic change password khac nhau
//    if (updateUserDto.getPassword() != null) {
//      user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
//    }
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
        // yeu cau admin moi co quyen cap quyen
//    if (updateUserDto.getRoles() != null) {
//      Arrays.stream(updateUserDto.getRoles())
//        .forEach(role -> {
//          if (roleService.existsByName(role)) {
//            user.getUserRoles().add(roleService.findByName(role));
//          }
//        });
//    }

        return userRepository.save(user);
    }

    public UserEntity updatePassUser(String password, Long id) {
        UserEntity userEntity = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (userEntity == null) {
            throw new NullPointerException("Không tìm thấy người dùng");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
    public UserEntity updateRolesUser(List<Long> newRoleIds, Long id) {
        UserEntity userEntity = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (userEntity == null) {
            throw new NullPointerException("Không tìm thấy người dùng");
        }
        // xoa het role cu, them lai role moi
        userEntity.getUserRoles().clear();

        List<RoleEntity> roles = roleRepository.findAllById(newRoleIds);

        for (RoleEntity role : roles) {
            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUser(userEntity);
            userRole.setRole(role);
            userEntity.getUserRoles().add(userRole);
        }


        return userRepository.save(userEntity);
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
}
