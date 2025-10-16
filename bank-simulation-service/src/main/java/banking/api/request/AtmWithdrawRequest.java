package banking.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AtmWithdrawRequest {
    @NotEmpty
    @NotBlank(message = "The field 'amount' is required")
    private double amount;

    private String description;
}
