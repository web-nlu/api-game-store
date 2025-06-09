package vn.edu.hcmaf.apigamestore.images;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "images")
public class ImagesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer position;

  @Column(columnDefinition = "text")
  private String image;

  private String entity;

  @Column(name = "entity_id")
  private Long entityId;

}
