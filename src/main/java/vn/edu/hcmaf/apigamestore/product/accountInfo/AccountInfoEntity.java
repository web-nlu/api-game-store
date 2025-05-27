package vn.edu.hcmaf.apigamestore.product.accountInfo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.common.util.AesUtil;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;


@Entity
@Table(name = "account_info")
@Data
public class AccountInfoEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Getter
    @Setter
    private String username;

    @NotNull
    private String password;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String status;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    public void setRawPassword(String rawPassword) {
        try {
            this.password = AesUtil.encrypt(rawPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    public String getDecryptedPassword() {
        try {
            return AesUtil.decrypt(this.password);
        } catch (Exception e) {
            return "123";
//            throw new RuntimeException("Error decrypting password", e);
        }
    }
}
