package banking.api.service;

import banking.api.domain.Account;
import banking.api.domain.Transaction;
import banking.api.exception.EmailAlreadyExistsException;
import banking.api.exception.NotFoundException;
import banking.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;



    public Account getAccountByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }
    public Account getAccountByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }


    public Account createAccount(Account account) {
        assertEmailDoesNotExists(account.getEmail());
        return repository.save(account);
    }




    public void assertEmailDoesNotExists(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwEmailExistsException);
    }
    private void throwEmailExistsException(Account account) {
        throw new EmailAlreadyExistsException("E-mail %s already exists".formatted(account.getEmail()));
    }


    public boolean fromAccountHasEnoughFundsForTransaction(String fromAccountNumber, double amount) {
        var fromAccount = getAccountByAccountNumber(fromAccountNumber);
        return fromAccount.getBalance() >= amount;
    }


    public void makeTransaction(Transaction transaction) {
        if (transaction.getFromAccountNumber() == null && transaction.getToAccountNumber() == null) {
            throw new NotFoundException("Not Found Account");
        }

        // If it's not a deposit
        if (transaction.getFromAccountNumber() != null) {
            var fromAccount = this.getAccountByAccountNumber(transaction.getFromAccountNumber());

            fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());

            repository.save(fromAccount);

        }

        // If it's not a withdrawal
        if (transaction.getToAccountNumber() != null) {

            var toAccount = this.getAccountByAccountNumber(transaction.getToAccountNumber());

            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());

            repository.save(toAccount);

        }



    }
}
