package vn.edu.hcmaf.apigamestore.category.controller.publicCategoryController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryService;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/category/u")
@RequiredArgsConstructor
/**
 * CategoryController handles public operations related to categories.
 * It provides endpoints for retrieving all categories and getting a category by its ID.
 */
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves all categories.
     * @return A response entity containing a list of all categories.
     */
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories().stream()
                .map((categoryEntity -> categoryService.toCategoryResponseDto(categoryEntity,false)))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all categories success", categories));
    }
    /**
     * Retrieves a category by its ID.
     * @param categoryId The ID of the category to be retrieved.
     * @return A response entity containing the category details.
     */
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID")
    @GetMapping("/{categoryId}")
    public ResponseEntity<BaseResponse> getCategoryById(@PathVariable long categoryId) {
        CategoryEntity category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get category by id success", categoryService.toCategoryResponseDto(category,true)));
    }


}
