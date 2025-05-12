package vn.edu.hcmaf.apigamestore.category;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public CategoryEntity getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public CategoryEntity addCategory(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    public CategoryEntity updateCategory(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    public void deleteCategory(long categoryId) {
        // Check if the category exists before deleting
        if (!categoryRepository.existsByIdAndIsDeletedFalse(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        CategoryEntity categoryEntity = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if (categoryEntity != null) {
            categoryEntity.setDeleted(true);
            categoryEntity.setDeletedAt(String.valueOf(java.time.LocalDateTime.now()));
            categoryEntity.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            categoryRepository.save(categoryEntity);
        } else {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }

    }
}
