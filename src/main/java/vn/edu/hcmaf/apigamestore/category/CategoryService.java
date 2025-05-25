package vn.edu.hcmaf.apigamestore.category;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GameService gameService;

    /**
     * Converts a CategoryEntity to a CategoryResponseDto.
     *
     * @param categoryEntity The CategoryEntity to convert.
     * @param includeGames   Whether to include games in the response.
     * @return A CategoryResponseDto containing the category details.
     */
    public CategoryResponseDto toCategoryResponseDto(CategoryEntity categoryEntity, boolean includeGames) {
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .games(new ArrayList<>())
                .build();
        System.out.println("CategoryEntity.toCategoryResponseDto: " + categoryResponseDto);
        if (includeGames) {
            categoryEntity.getGames().forEach(game -> {
                categoryResponseDto.getGames().add(gameService.toGameResponseDto(game, true));
            });
        }
        return categoryResponseDto;
    }
    /**
     * Retrieves all categories that are not deleted.
     *
     * @return A list of CategoryEntity objects representing all non-deleted categories.
     */
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }
    /**
     * Retrieves a category by its ID if it is not deleted.
     *
     * @param categoryId The ID of the category to retrieve.
     * @return A CategoryEntity representing the category with the specified ID, or null if not found.
     */
    public CategoryEntity getCategoryById(long categoryId) {
        return categoryRepository.findByIdAndIsDeletedFalse(categoryId);
    }
    /**
     * Adds a new category.
     *
     * @param categoryRequestDto The request DTO containing the category details.
     * @return A CategoryEntity representing the newly added category.
     */
    public CategoryEntity addCategory(AddCategoryRequestDto categoryRequestDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryRequestDto.getName());
        categoryEntity.setIcon(categoryRequestDto.getIcon());
        return categoryRepository.save(categoryEntity);
    }
/**
     * Updates an existing category by its ID.
     *
     * @param addCategoryRequestDto The request DTO containing the updated category details.
     * @param categoryId            The ID of the category to update.
     * @return A CategoryEntity representing the updated category.
     */
    public CategoryEntity updateCategory(AddCategoryRequestDto addCategoryRequestDto, long categoryId) {
        // Check if the category exists before updating
        if (!categoryRepository.existsByIdAndIsDeletedFalse(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        CategoryEntity existingCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        existingCategory.setName(addCategoryRequestDto.getName());
        existingCategory.setIcon(addCategoryRequestDto.getIcon());
        return categoryRepository.save(existingCategory);

    }
    /**
     * Deletes a category by its ID.
     * Marks the category as deleted and sets the deletion timestamp and user.
     *
     * @param categoryId The ID of the category to delete.
     */
    public void deleteCategory(long categoryId) {
        // Check if the category exists before deleting
        if (!categoryRepository.existsByIdAndIsDeletedFalse(categoryId)) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }
        CategoryEntity categoryEntity = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if (categoryEntity != null) {
            categoryEntity.setDeleted(true);
            categoryEntity.setDeletedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
            categoryEntity.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            categoryRepository.save(categoryEntity);
        } else {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }

    }
}
