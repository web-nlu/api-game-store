package vn.edu.hcmaf.apigamestore.category;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GameService gameService;


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

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findByIsDeletedFalse();
    }

    public CategoryEntity getCategoryById(long categoryId) {
        return categoryRepository.findByIdAndIsDeletedFalse(categoryId);
    }

    public CategoryEntity addCategory(AddCategoryRequestDto categoryRequestDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryRequestDto.getName());
        categoryEntity.setIcon(categoryRequestDto.getIcon());
        return categoryRepository.save(categoryEntity);
    }

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
