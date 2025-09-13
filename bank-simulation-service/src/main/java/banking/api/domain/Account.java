package banking.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account implements UserDetails {
    // Id for database purposes (Users are not supposed to have access)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;


    // Number people can see
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null) {
            this.accountNumber = String.format("%09d", System.currentTimeMillis() % 999_999_999);
        }
    }


    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String password;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
