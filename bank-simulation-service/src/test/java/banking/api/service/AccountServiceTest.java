package banking.api.service;

import banking.api.commons.AccountUtils;
import banking.api.domain.Account;
import banking.api.exception.EmailAlreadyExistsException;
import banking.api.mapper.AccountMapper;
import banking.api.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceTest {

    @InjectMocks
    private AccountUtils accountUtils;
    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountMapper mapper;

    private List<Account> accountsList;

    @BeforeEach
    void init() {
        accountsList = accountUtils.newAccountList();
    }


    @Test
    @DisplayName("findByEmail returns found object when email exists")
    @Order(1)
    void findByEmail_ReturnsFoundAccount_WhenEmailIsFound() {
        var account = accountsList.getFirst();

        BDDMockito.when(repository.findByEmail(account.getEmail())).thenReturn(Optional.of(account));

        var accountFound = service.getAccountByEmail(account.getEmail());

        Assertions.assertThat(accountFound).isEqualTo(account);

    }

    @Test
    @DisplayName("findByEmail returns nothing when Email is not Found")
    @Order(2)
    void findByEmail_ThrowsResponseStatusException_WhenEmailIsNotFound() {
        Account expectedAccount = accountsList.getFirst();
        BDDMockito.when(repository.findByEmail(expectedAccount.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.getAccountByEmail(expectedAccount.getEmail()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("findByAccountNumber returns account with given AccountNumber")
    @Order(3)
    void findById_ReturnsAccount_WhenSuccessful() {
        Account expectedAccount = accountsList.getFirst();
        BDDMockito.when(repository.findByAccountNumber(expectedAccount.getAccountNumber())).thenReturn(Optional.of(expectedAccount));

        var account = service.getAccountByAccountNumber(expectedAccount.getAccountNumber());

        Assertions.assertThat(account).isEqualTo(expectedAccount);

    }

    @Test
    @DisplayName("findByAccountNumber throws ResponseStatusException when account is not found")
    @Order(4)
    void findById_ThrowsResponseStatusException_WhenAccountIsNotFound() {
        Account expectedAccount = accountsList.getFirst();
        BDDMockito.when(repository.findByAccountNumber(expectedAccount.getAccountNumber())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.getAccountByAccountNumber(expectedAccount.getAccountNumber()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("save creates account when successful")
    @Order(6)
    void save_CreatesAccount_WhenSuccessful() {
        var accountSaved = accountUtils.newAccountSaved();

        BDDMockito.when(repository.save(accountSaved)).thenReturn(accountSaved);
        BDDMockito.when(repository.findByEmail(accountSaved.getEmail())).thenReturn(Optional.empty());

        var savedAccount = service.createAccount(accountSaved);

        Assertions.assertThat(savedAccount).isEqualTo(accountSaved).hasNoNullFieldsOrProperties();

    }


    @Test
    @DisplayName("save throws EmailAlreadyExistsException when email exists")
    @Order(12)
    void save_ThrowsEmailAlreadyExistsException_WhenEmailExists() {
        var savedAccount = accountsList.getLast();
        var accountToSave = accountUtils.newAccountToSave().withEmail(savedAccount.getEmail());
        var email = accountToSave.getEmail();

        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedAccount));

        Assertions.assertThatException()
                .isThrownBy(() -> service.createAccount(accountToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);

    }
}