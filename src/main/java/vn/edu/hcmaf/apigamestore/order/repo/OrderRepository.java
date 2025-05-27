package vn.edu.hcmaf.apigamestore.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByUserId(Long id);
}
