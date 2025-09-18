package banking.api.service;

import banking.api.domain.Account;
import banking.api.domain.Transaction;
import banking.api.exception.NotEnoughFundsException;
import banking.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository repository;

    public Account getAccountByEmail(String email) {
        return accountService.getAccountByEmail(email);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return repository.findTransactionsByAccountNumber(accountNumber);
    }


    public Transaction makeTransaction(Transaction transaction) {
        assertFromAccountHasEnoughFundsForTransaction(
                transaction.getFromAccountNumber(),
                transaction.getAmount()
        );

        accountService.makeTransaction(transaction);

        return repository.save(transaction);
    }

    public void makeDeposit(Transaction transaction) {
        accountService.makeTransaction(transaction);
    }

    public void makeWithdraw(Transaction transaction) {
        assertFromAccountHasEnoughFundsForTransaction(
                transaction.getFromAccountNumber(),
                transaction.getAmount()
        );

        accountService.makeTransaction(transaction);
    }


    private void assertFromAccountHasEnoughFundsForTransaction(String fromAccountNumber, double amount) {
        if (!accountService.fromAccountHasEnoughFundsForTransaction(fromAccountNumber, amount)) {
            this.throwNotEnoughFunds();
        }
    }

    private void throwNotEnoughFunds() {
        throw new NotEnoughFundsException("Insufficient funds to complete the transaction");
    }

}
