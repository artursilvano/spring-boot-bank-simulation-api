package banking.api.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountGetResponse {
    private String accountNumber;
    private String ownerName;
    private String email;


}
