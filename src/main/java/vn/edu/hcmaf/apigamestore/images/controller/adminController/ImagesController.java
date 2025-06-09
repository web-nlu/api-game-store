package vn.edu.hcmaf.apigamestore.images.controller.adminController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.images.ImagesEntity;
import vn.edu.hcmaf.apigamestore.images.ImagesService;
import vn.edu.hcmaf.apigamestore.images.dto.ImagesDTO;

import java.util.List;

@RestController
@RequestMapping("/api/admin/images")
public class ImagesController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImagesEntity> updateImage(
            @PathVariable Long id,
            @RequestBody ImagesDTO imageDTO) {
        ImagesEntity updated = imagesService.updateImage(id, imageDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long id) {
        imagesService.deleteImage(id);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Xoá thành công", null));
    }
}
