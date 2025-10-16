package banking.api.controller;


import banking.api.domain.Transaction;
import banking.api.request.AtmDepositRequest;
import banking.api.request.AtmWithdrawRequest;
import banking.api.service.AtmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/atm")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank API", description = "ATM related endpoints")
public class AtmController {

    private final AtmService service;

    @PostMapping("/deposit")
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

        return ResponseEntity.ok().build();
    }

    @PostMapping("withdraw")
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

        return ResponseEntity.ok().build();
    }


}
