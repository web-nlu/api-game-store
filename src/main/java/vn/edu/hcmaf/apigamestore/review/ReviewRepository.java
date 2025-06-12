package vn.edu.hcmaf.apigamestore.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByAccountId(Long accountId);
}
