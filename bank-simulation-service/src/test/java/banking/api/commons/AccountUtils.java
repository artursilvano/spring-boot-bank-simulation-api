package banking.api.commons;


import banking.api.domain.Account;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AccountUtils {

    public List<Account> newAccountList() {
        var pedro = Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("111111111")
                .ownerName("Pedro")
                .email("pedro@gmail.com")
                .balance(1)
                .password("{bcrypt}$2a$10$0yPaTu36qTuDant0bIXNA.UxSYDiVX37uWkvF6OY.Rub371e.qvbK")
                .build();

        var joao = Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("222222222")
                .ownerName("Joao")
                .email("joao@gmail.com")
                .balance(2)
                .password("{bcrypt}$2a$10$0yPaTu36qTuDant0bIXNA.UxSYDiVX37uWkvF6OY.Rub371e.qvbK")
                .build();

        var jose = Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("333333333")
                .ownerName("Jose")
                .email("jose@gmail.com")
                .balance(3)
                .password("{bcrypt}$2a$10$0yPaTu36qTuDant0bIXNA.UxSYDiVX37uWkvF6OY.Rub371e.qvbK")
                .build();


        return List.of(pedro, joao, jose);
    }

    public Account newAccountToSave() {
        return Account.builder()
                .ownerName("ownerName")
                .email("ownerEmail")
                .password("{bcrypt}$2a$10$0yPaTu36qTuDant0bIXNA.UxSYDiVX37uWkvF6OY.Rub371e.qvbK")           // ENCRIPTAR ANTES
                .build();
    }

    public Account newAccountSaved() {
        return Account.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123456789")
                .ownerName("ownerName")
                .email("ownerEmail")
                .balance(0.0)
                .password("{bcrypt}$2a$10$0yPaTu36qTuDant0bIXNA.UxSYDiVX37uWkvF6OY.Rub371e.qvbK")
                .build();
    }

}
