package banking.api.service;

import banking.api.domain.Account;
import banking.api.mapper.AccountMapper;
import banking.api.repository.AccountRepository;
import banking.api.request.AccountPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Qualifier("accountMapper")
    private  final AccountMapper mapper;
    private final AccountRepository repository;


    public Account findAccount(UUID accountId) {

        return repository.findById(accountId).orElse(null);

    }

    public Account createAccount(AccountPostRequest accountPostRequest) {

        var account = mapper.toAccount(accountPostRequest);
        return repository.save(account);

    }

}
