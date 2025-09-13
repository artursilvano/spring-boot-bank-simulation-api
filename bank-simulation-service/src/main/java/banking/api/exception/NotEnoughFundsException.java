package banking.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotEnoughFundsException extends ResponseStatusException {
    public NotEnoughFundsException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
