package banking.api.controller;

import banking.api.mapper.AccountMapper;
import banking.api.request.AccountPostRequest;
import banking.api.response.AccountGetResponse;
import banking.api.response.AccountOwnedGetResponse;
import banking.api.response.AccountPostResponse;
import banking.api.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("v1/account")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank API", description = "Account related endpoints")
public class AccountController {

    @Qualifier("accountMapper")
    private final AccountMapper mapper;
    private final AccountService service;

    // YOUR ACCOUNT INFO
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountOwnedGetResponse> getAccountDetails(Principal principal) {
        log.info("Get your account details. email: {}", principal.getName());

        var account = service.getAccountByEmail(principal.getName());

        var response = mapper.toAccountOwnedGetResponse(account);

        return ResponseEntity.ok(response);
    }

    // GET ACCOUNT INFO BY ID
    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
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
    public ResponseEntity<AccountPostResponse> createAccount(@RequestBody @Valid AccountPostRequest accountPostRequest) {
        log.info("Create account");

        var account = mapper.toAccount(accountPostRequest);

        var createdAccount = service.createAccount(account);

        var response = mapper.toAccountPostResponse(createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
