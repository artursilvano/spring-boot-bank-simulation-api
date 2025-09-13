package banking.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @Column(nullable = false)
    private String fromAccountNumber;

    @Column
    private String toAccountNumber;

    @Column(nullable = false)
    private double amount;
}
