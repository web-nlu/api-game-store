package vn.edu.hcmaf.apigamestore.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<ImagesEntity, Long> {
    List<ImagesEntity> findByEntityAndEntityIdOrderByPositionAsc(String entity, Long entityId);
}
