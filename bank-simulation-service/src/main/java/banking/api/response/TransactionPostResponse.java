package banking.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPostResponse {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
}
