package vn.edu.hcmaf.apigamestore.category.controller.AdminCategoryController;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryService;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
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
