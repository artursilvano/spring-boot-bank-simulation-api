package banking.api.repository;

import banking.api.commons.AccountUtils;
import banking.api.config.IntegrationTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(AccountUtils.class)
class AccountRepositoryIT extends IntegrationTestConfig {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private AccountUtils accountUtils;

    @Test
    @DisplayName("save creates account when successful")
    @Order(1)
    void save_CreatesAccount_WhenSuccessful() {
        var accountToSave = accountUtils.newAccountToSave();
        var savedAccount = repository.save(accountToSave);

        Assertions.assertThat(savedAccount).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedAccount.getAccountNumber()).isNotNull();

    }

    @Test
    @DisplayName("findAll returns a list with all accounts")
    @Order(2)
    @Sql("/sql/account/init_one_account.sql")
    void findAll_ReturnsAllAccounts_WhenSuccessful() {

        var accounts = repository.findAll();
        Assertions.assertThat(accounts).isNotEmpty();

    }

}