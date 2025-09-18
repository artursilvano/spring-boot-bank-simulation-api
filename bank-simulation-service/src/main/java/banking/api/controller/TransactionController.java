package banking.api.controller;

import banking.api.domain.Transaction;
import banking.api.mapper.TransactionMapper;
import banking.api.request.TransactionPostRequest;
import banking.api.response.TransactionGetResponse;
import banking.api.response.TransactionPostResponse;
import banking.api.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/transaction")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank API", description = "Transaction related endpoints")
public class TransactionController {
    private final TransactionService service;
    @Qualifier("transactionMapper")
    private final TransactionMapper mapper;

    @GetMapping("/history")
    public ResponseEntity<List<TransactionGetResponse>> getTransactionHistory(Principal principal) {
        var fromAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transactions = service.getTransactionHistory(fromAccountNumber);

        var response = mapper.toTransactionGetResponse(transactions);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionPostResponse> makeTransaction(Principal principal, @RequestBody TransactionPostRequest transactionRequest) {
        var fromAccountNumber = service.getAccountByEmail(principal.getName()).getAccountNumber();

        var transaction = Transaction.builder()
                .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(transactionRequest.getToAccountNumber())
                .amount(transactionRequest.getAmount())
                .build();

        var result = service.makeTransaction(transaction);

        var response = mapper.toTransactionPostResponse(result);

        return ResponseEntity.accepted().body(response);

    }


}
