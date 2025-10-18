package banking.api.controller;

import banking.api.domain.Transaction;
import banking.api.exception.DefaultErrorMessage;
import banking.api.mapper.TransactionMapper;
import banking.api.request.TransactionPostRequest;
import banking.api.response.AccountPostResponse;
import banking.api.response.TransactionGetResponse;
import banking.api.response.TransactionPostResponse;
import banking.api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/transaction")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Transaction related endpoints")
public class TransactionController {
    private final TransactionService service;
    @Qualifier("transactionMapper")
    private final TransactionMapper mapper;


    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get transaction history",
            description = "Get the transaction history for the authenticated user's account",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transaction history",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TransactionGetResponse.class))
                            )
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
    public ResponseEntity<List<TransactionGetResponse>> getTransactionHistory(Principal principal) {
        var fromAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transactions = service.getTransactionHistory(fromAccountNumber);

        var response = mapper.toTransactionGetResponse(transactions);

        return ResponseEntity.ok(response);
    }




    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Make a transaction",
            description = "Make a transaction from the authenticated user's account to another account",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Successfully made transaction",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TransactionPostResponse.class)
                            )
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
    public ResponseEntity<TransactionPostResponse> makeTransaction(Principal principal,
                                                                   @RequestBody TransactionPostRequest transactionRequest) {
        var fromAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transaction = Transaction.builder()
                .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(transactionRequest.getToAccountNumber())
                .amount(transactionRequest.getAmount())
                .description(transactionRequest.getDescription())
                .createdAt(LocalDateTime.now().toString())
                .build();

        var result = service.makeTransaction(transaction);

        var response = mapper.toTransactionPostResponse(result);

        return ResponseEntity.accepted().body(response);

    }


}
