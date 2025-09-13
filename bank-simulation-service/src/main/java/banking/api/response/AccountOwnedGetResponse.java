package banking.api.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AccountOwnedGetResponse extends AccountGetResponse {
    private String balance;

}
