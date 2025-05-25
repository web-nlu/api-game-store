package vn.edu.hcmaf.apigamestore.category.controller.AdminCategoryController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryService;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
/**
 * AdminCategoryController handles administrative operations related to categories.
 * It provides endpoints for adding, updating, and deleting categories.
 */
public class AdminCategoryController {
    private final CategoryService categoryService;
    /**
     * Retrieves all categories.
     * @return A response entity containing a list of all categories.
     */
    @Operation(summary = "Get all categories", description = "Retrieve all categories")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addCategory(@RequestBody AddCategoryRequestDto categoryRequestDto) {
        CategoryEntity newCategory = categoryService.addCategory(categoryRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add category success", newCategory));
    }
    /**
     * Updates an existing category by its ID.
     * @param categoryRequestDto The request body containing the updated category details.
     * @param categoryId The ID of the category to be updated.
     * @return A response entity indicating the success of the update operation.
     */
    @Operation(summary = "Update category", description = "Update an existing category by ID")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<BaseResponse> updateCategory(@RequestBody AddCategoryRequestDto categoryRequestDto, @PathVariable long categoryId) {
        CategoryEntity updatedCategory = categoryService.updateCategory(categoryRequestDto, categoryId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update category success", updatedCategory));
    }
    /**
     * Deletes a category by its ID.
     * @param categoryId The ID of the category to be deleted.
     * @return A response entity indicating the success of the delete operation.
     */
    @Operation(summary = "Delete category", description = "Delete a category by ID")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(Long.parseLong(categoryId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete category success", null));
    }
}
