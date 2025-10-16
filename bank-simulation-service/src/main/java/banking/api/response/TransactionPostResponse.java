package banking.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPostResponse {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String amount;
    private String description;
    private String createdAt;
}
