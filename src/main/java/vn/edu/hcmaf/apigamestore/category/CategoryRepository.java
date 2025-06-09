package vn.edu.hcmaf.apigamestore.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsByIdAndIsDeletedFalse(long categoryId);

    CategoryEntity findByIdAndIsDeletedFalse(long categoryId);

    List<CategoryEntity> findByIsDeletedFalse();
}
