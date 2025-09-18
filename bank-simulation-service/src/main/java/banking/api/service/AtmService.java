package banking.api.service;

import banking.api.domain.Account;
import banking.api.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final TransactionService transactionService;

    public Account getAccountByEmail(String email) {
        return transactionService.getAccountByEmail(email);
    }




    public void deposit(Transaction transaction) {

        transactionService.makeDeposit(transaction);

    }

    public void withdraw(Transaction transaction) {

        transactionService.makeWithdraw(transaction);

    }


}
