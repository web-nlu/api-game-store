package vn.edu.hcmaf.apigamestore.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByUser(UserEntity user);

    boolean existsByUserIdAndAccountId(Long id, Long id1);

    List<CartEntity> findByUserId(Long id);
}
