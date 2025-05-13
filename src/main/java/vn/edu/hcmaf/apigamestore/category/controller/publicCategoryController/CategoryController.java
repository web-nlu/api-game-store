package vn.edu.hcmaf.apigamestore.category.controller.publicCategoryController;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.CategoryService;
import vn.edu.hcmaf.apigamestore.category.dto.AddCategoryRequestDto;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameRepository;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/category/u")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;


    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories().stream()
                .map((categoryEntity -> categoryService.toCategoryResponseDto(categoryEntity,false)))
                .toList();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all categories success", categories));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<BaseResponse> getCategoryById(@PathVariable long categoryId) {
        CategoryEntity category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get category by id success", categoryService.toCategoryResponseDto(category,true)));
    }


}
