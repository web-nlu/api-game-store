package vn.edu.hcmaf.apigamestore.images;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.images.dto.ImagesDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagesService {

    private final ImagesRepository imagesRepository;

    public ImagesService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

  public List<ImagesEntity> setImages(String entity, Long entityId, List<ImagesDTO> imageDTOs) {
    List<ImagesEntity> images = imageDTOs.stream().map(dto -> {
      ImagesEntity image = new ImagesEntity();
      if(dto.getId() != null) {
        image.setId(dto.getId());
      }
      image.setEntity(entity);
      image.setEntityId(entityId);
      image.setPosition(dto.getPosition());
      image.setImage(dto.getImage());
      return image;
    }).collect(Collectors.toList());

    return imagesRepository.saveAll(images);
  }

    public ImagesEntity updateImage(Long id, ImagesDTO imageDTO) {
        ImagesEntity existingImage = imagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));
        existingImage.setPosition(imageDTO.getPosition());
        existingImage.setImage(imageDTO.getImage());
        return imagesRepository.save(existingImage);
    }

    public void deleteImage(Long id) {
        ImagesEntity existingImage = imagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));
        imagesRepository.delete(existingImage);
    }

    public List<ImagesEntity> getImagesByEntity(String entity, Long entityId) {
        return imagesRepository.findByEntityAndEntityIdOrderByPositionAsc(entity, entityId);
    }
}
