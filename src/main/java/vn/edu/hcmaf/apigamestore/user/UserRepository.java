package vn.edu.hcmaf.apigamestore.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>  {
   Optional<UserEntity> findByUsername(String username);
   Optional<UserEntity> findById(Long id);
   Boolean existsByUsername(String username);

   List<UserEntity> findAllByIsDeletedFalse(Pageable pageable);

   Long countByIsDeletedFalse();

   Optional<UserEntity> findByIdAndIsDeletedFalse(Long id);
}
