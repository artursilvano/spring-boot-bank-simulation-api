package banking.api.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
