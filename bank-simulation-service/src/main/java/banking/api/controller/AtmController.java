package banking.api.controller;


import banking.api.domain.Transaction;
import banking.api.exception.DefaultErrorMessage;
import banking.api.request.AtmDepositRequest;
import banking.api.request.AtmWithdrawRequest;
import banking.api.response.TransactionPostResponse;
import banking.api.service.AtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/atm")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "ATM Controller", description = "ATM related endpoints")
public class AtmController {

    private final AtmService service;

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Make a withdrawal",
            description = "Make a withdrawal from your account using an ATM",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Successfully made withdrawal"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - Invalid credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> deposit(Principal principal, @RequestBody AtmDepositRequest depositRequest) {

        var toAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transaction = Transaction.builder()
                .fromAccountNumber(null)
                .toAccountNumber(toAccountNumber)
                .amount(depositRequest.getAmount())
                .description(depositRequest.getDescription())
                .createdAt(LocalDateTime.now().toString())
                .build();

        service.deposit(transaction);

        return ResponseEntity.accepted().build();
    }






    @PostMapping("withdraw")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Make a withdrawal",
            description = "Make a withdrawal from your account using an ATM",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Successfully made withdrawal"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - Invalid credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Insufficient funds to complete the transaction",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> withdraw(Principal principal, @RequestBody AtmWithdrawRequest withdrawRequest) {

        var fromAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transaction = Transaction.builder()
                .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(null)
                .amount(withdrawRequest.getAmount())
                .description(withdrawRequest.getDescription())
                .createdAt(LocalDateTime.now().toString())
                .build();

        service.withdraw(transaction);

        return ResponseEntity.accepted().build();
    }


}
