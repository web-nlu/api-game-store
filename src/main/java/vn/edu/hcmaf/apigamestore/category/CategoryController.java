package vn.edu.hcmaf.apigamestore.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllCategories() {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all categories success", categories));
    }
    @PostMapping("/add")
    public ResponseEntity<BaseResponse> addCategory(@RequestBody CategoryEntity categoryEntity) {
        CategoryEntity newCategory = categoryService.addCategory(categoryEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add category success", newCategory));
    }
    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateCategory(CategoryEntity categoryEntity) {
        CategoryEntity updatedCategory = categoryService.updateCategory(categoryEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update category success", updatedCategory));
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(Long.parseLong(categoryId));
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete category success", null));
    }
}
