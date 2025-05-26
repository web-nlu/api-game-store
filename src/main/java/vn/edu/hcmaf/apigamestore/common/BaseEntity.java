package vn.edu.hcmaf.apigamestore.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
// This class serves as a base entity for all entities in the application, providing common fields and lifecycle methods.
public class BaseEntity {
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
    @Column(name = "deleted_by")
    private String deletedBy;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    /* * The `BaseEntity` class is a base class for all entities in the application.
     * It provides common fields such as `isDeleted`, `deletedAt`, `deletedBy`, `createdAt`, and `updatedAt`.
     * It also includes lifecycle methods to automatically set the timestamps when an entity is created or updated.
     */

    @PrePersist
    // This method is called before the entity is persisted to the database.
    protected void onCreate() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
        this.isDeleted = false;
    }
    @PreUpdate
    // This method is called before the entity is updated in the database.
    protected void onUpdate() {
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
