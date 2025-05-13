package vn.edu.hcmaf.apigamestore.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameRepository;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @GetMapping("/u/all")
    public ResponseEntity<BaseResponse> getAllCategories() {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        List<CategoryResponseDto> categoryResponseDtos = categories.stream()
                .map((categoryEntity -> categoryService.toCategoryResponseDto(categoryEntity,false)))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all categories success", categoryResponseDtos));
    }

    @GetMapping("/u/{categoryId}")
    public ResponseEntity<BaseResponse> getCategoryById(@PathVariable long categoryId) {
        CategoryEntity category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get category by id success", categoryService.toCategoryResponseDto(category,true)));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addCategory(@RequestBody AddCategoryRequestDto categoryRequestDto) {
        CategoryEntity newCategory = categoryService.addCategory(categoryRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add category success", newCategory));
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<BaseResponse> updateCategory(@RequestBody AddCategoryRequestDto categoryRequestDto, @PathVariable long categoryId) {
        CategoryEntity updatedCategory = categoryService.updateCategory(categoryRequestDto, categoryId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update category success", updatedCategory));
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(Long.parseLong(categoryId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete category success", null));
    }
}
