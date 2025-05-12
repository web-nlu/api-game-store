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

  public List<UserEntity> getAllUsers() {
    return userRepository.findAll();
  }

  public UserEntity getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity userEntity = userRepository.findByEmail(username).orElse(null);
    if(userEntity == null) {
      throw new IllegalArgumentException("Người dùng không tồn tại");
    }
    userEntity.setPassword(null);
    return userEntity;
  }

  public LazyLoadingResponseDto<List<UserEntity>> getAllUsersLazyLoading(LazyLoadingRequestDto<Sort> lazyLoadingRequestDto) {
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
    if(userEntity == null) {
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
            user.getRoles().add(roleService.findByName(role));
          }
        });
    }

    return userRepository.save(user);
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
    userEntity.setDeletedAt(String.valueOf(LocalDateTime.now()));
    userEntity.setDeletedBy(currentUser);
    userRepository.save(userEntity);
    return true;
  }
}
