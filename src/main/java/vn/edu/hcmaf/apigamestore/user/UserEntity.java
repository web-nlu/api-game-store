package vn.edu.hcmaf.apigamestore.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.cart.CartEntity;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleEntity;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    private String refreshToken;
    private String avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserRoleEntity> userRoles;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartEntity> cartItems;

    @Transient
    public List<RoleEntity> getActiveRoles() {
        return userRoles.stream()
                .filter(userRole -> !userRole.isDeleted())
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toList());
    }
}

