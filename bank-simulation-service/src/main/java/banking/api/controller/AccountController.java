package banking.api.controller;

import banking.api.domain.Account;
import banking.api.request.AccountPostRequest;
import banking.api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;



    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountPostRequest accountPostRequest) {

        var account = service.createAccount(accountPostRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }




}
