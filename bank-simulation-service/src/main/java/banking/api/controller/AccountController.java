package banking.api.controller;

import banking.api.exception.DefaultErrorMessage;
import banking.api.mapper.AccountMapper;
import banking.api.request.AccountPostRequest;
import banking.api.response.AccountGetResponse;
import banking.api.response.AccountOwnedGetResponse;
import banking.api.response.AccountPostResponse;
import banking.api.response.AuthResponse;
import banking.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/account")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "Account related endpoints")
public class AccountController {

    @Qualifier("accountMapper")
    private final AccountMapper mapper;
    private final AccountService service;

    // YOUR ACCOUNT INFO
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Account info",
            description = "Retrieve your account information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of account information",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountOwnedGetResponse.class)
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
    public ResponseEntity<AccountOwnedGetResponse> getAccountDetails(Principal principal) {
        log.info("Get your account details. email: {}", principal.getName());

        var account = service.getAccountByEmail(principal.getName());

        var response = mapper.toAccountOwnedGetResponse(account);

        return ResponseEntity.ok(response);
    }

    // GET ACCOUNT INFO BY ID
    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Other account info",
            description = "Retrieve other account information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval of account information",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountGetResponse.class)
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
    public ResponseEntity<AccountGetResponse> getAccountDetails(@PathVariable String accountNumber, Principal principal) {
        log.info("Get account details for {}", accountNumber);

        AccountGetResponse response;
        var account = service.getAccountByAccountNumber(accountNumber);

        // Show balance if the authenticated user is the account owner
        if (principal.getName().equals(account.getUsername())) {
            response = mapper.toAccountOwnedGetResponse(account);
        } else {
            response = mapper.toAccountGetResponse(account);
        }

        return ResponseEntity.ok(response);
    }

    // CREATE ACCOUNT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create account",
            description = "Create a new account in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully created account",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AccountPostResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - E-mail is already in use or invalid data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<AccountPostResponse> createAccount(@RequestBody @Valid AccountPostRequest accountPostRequest) {
        log.info("Create account");

        var account = mapper.toAccount(accountPostRequest);

        var createdAccount = service.createAccount(account);

        var response = mapper.toAccountPostResponse(createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
