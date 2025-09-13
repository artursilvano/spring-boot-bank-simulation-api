package banking.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountPostRequest {
    @NotBlank(message = "The field 'ownerName' is required")
    private String ownerName;

    @NotBlank(message = "The field 'email' is required")
    @Email(message = "The e-mail is not valid")
    private String email;

    @NotBlank(message = "The field 'password' is required")
    private String password;
}
