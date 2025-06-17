package vn.edu.hcmaf.apigamestore.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmaf.apigamestore.user.dto.UserResponseDto;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Boolean existsByUsername(String username);

    List<UserEntity> findAllByIsDeletedFalse(Pageable pageable);

    Long countByIsDeletedFalse();

    Optional<UserEntity> findByIdAndIsDeletedFalse(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIsDeletedFalse(String email);

    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);

    @Query("""
        SELECT u
        FROM UserEntity u
        INNER JOIN UserRoleEntity ur ON u.id = ur.user.id
        WHERE 1 = 1
            AND (:email is null OR u.email = :email)
            AND (:role is null OR ur.role.name = :role)
            AND u.isDeleted = false
    """)
    List<UserEntity> filter(@Param("email") String email, @Param("role") String role, Pageable pageable);

}
