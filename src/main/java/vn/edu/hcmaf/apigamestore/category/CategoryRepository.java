package vn.edu.hcmaf.apigamestore.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByIdAndIsDeletedFalse(long categoryId);

    Optional<CategoryEntity> findByIdAndIsDeletedFalse(long categoryId);

    List<CategoryEntity> findByIsDeletedFalse();
}
