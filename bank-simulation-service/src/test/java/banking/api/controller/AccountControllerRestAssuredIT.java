package banking.api.controller;

import banking.api.commons.FileUtils;
import banking.api.config.IntegrationTestConfig;
import banking.api.config.RestAssuredConfig;
import banking.api.repository.AccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/v1/account";

    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository repository;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularAccount")
    private RequestSpecification requestSpecificationRegularAccount;
    @Autowired
    @Qualifier(value = "requestSpecificationAdminAccount")
    private RequestSpecification requestSpecificationAdminAccount;

    @BeforeEach
    void setUp() {
        RestAssured.requestSpecification = requestSpecificationRegularAccount;
    }

/*
    @Test
    @DisplayName("GET v1/account/1 returns a account with given account number")
    @Sql(value = "/sql/account/init_three_accounts.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(1)
    void findById_ReturnsAccount_WhenSuccessful() {
        var expectedResponse = fileUtils.readResourceFile("account/get-account-by-accountNumber-200.json");
        var account = repository.findByEmail("emailOne@gmail.com");
        assertThat(account).isNotEmpty();

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("accountNumber", account.get().getAccountNumber())
                .get(URL + "/{accountNumber}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("accountNumber")
                .isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("GET v1/account/1 throws NotFoundException 404 when account is not found")
    @Sql(value = "/sql/account/init_three_accounts.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(2)
    void findById_ThrowsNotFoundException_WhenAccountIsNotFound() {
        var expectedResponse = fileUtils.readResourceFile("account/get-account-by-id-404.json");
        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/account?firstName=Toyohisa returns list with found object when first name exists")
    @Sql(value = "/sql/account/init_three_accounts.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(2)
    void findByName_ReturnsFoundAccountInList_WhenFirstNameIsFound() {
        RestAssured.requestSpecification = requestSpecificationAdminAccount;

        var expectedResponse = fileUtils.readResourceFile("account/get-account-toyohisa-first-name-200.json");
        var firstName = "Toyohisa";

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", firstName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);

    }

    @Test
    @DisplayName("GET v1/account?firstName=x returns empty list when first name is not found")
    @Sql(value = "/sql/account/init_three_accounts.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        RestAssured.requestSpecification = requestSpecificationAdminAccount;

        var expectedResponse = fileUtils.readResourceFile("account/get-account-x-first-name-200.json");
        var firstName = "x";

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("firstName", firstName)
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);

    }



    @Test
    @DisplayName("POST v1/account creates a account")
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(6)
    void save_CreatesAccount_WhenSuccessful() {
        var request = fileUtils.readResourceFile("account/post-request-account-200.json");
        var expectedResponse = fileUtils.readResourceFile("account/post-response-account-201.json");

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);


    }


    @ParameterizedTest
    @MethodSource("postAccountBadRequestSource")
    @DisplayName("POST v1/account returns bad request when fields are empty, blank or not valid")
    @Sql(value = "/sql/account/init_one_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("account/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("account/%s".formatted(responseFile));

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> postAccountBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-account-empty-fields-400.json", "post-response-account-empty-fields-400.json"),
                Arguments.of("post-request-account-blank-fields-400.json", "post-response-account-blank-fields-400.json"),
                Arguments.of("post-request-account-invalid-email-400.json", "post-response-account-invalid-email-400.json")
        );
    }
    */
    /*
    @Test
    @DisplayName("DELETE v1/account/1 removes a account")
    @Sql(value = "/sql/account/init_one_login_admin_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(7)
    void delete_RemoveAccount_WhenSuccessful() {
        RestAssured.requestSpecification = requestSpecificationAdminAccount;

        var id = repository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }

    @Test
    @DisplayName("DELETE v1/account/99 throws NotFoundException when account is not found")
    @Sql(value = "/sql/account/init_one_login_admin_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(8)
    void delete_ThrowsNotFoundException_WhenAccountIsNotFound() {
        RestAssured.requestSpecification = requestSpecificationAdminAccount;

        var expectedResponse = fileUtils.readResourceFile("account/delete-account-by-id-404.json");
        var id = 99L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }

    @Test
    @DisplayName("PUT v1/account updates a account")
    @Sql(value = "/sql/account/init_one_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(9)
    void update_UpdatesAccount_WhenSuccessful() {
        var request = fileUtils.readResourceFile("account/put-request-account-200.json");

        var accounts = repository.findByFirstNameIgnoreCase("Ash");
        var oldAccount = accounts.getFirst();
        assertThat(accounts).hasSize(1);
        request = request.replace("1", accounts.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var updatedAccount = repository.findById(oldAccount.getId()).orElseThrow(() -> new NotFoundException("Account not Found"));
        var encryptedPassword = updatedAccount.getPassword();
        assertThat(passwordEncoder.matches("leigan", encryptedPassword)).isTrue();

    }

    @Test
    @DisplayName("PUT v1/account throws NotFoundException when account is not found")
    @Sql(value = "/sql/account/init_one_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(10)
    void update_ThrowsNotFoundException_WhenAccountToUpdateIsNotFound() {
        var request = fileUtils.readResourceFile("account/put-request-account-404.json");
        var expectedResponse = fileUtils.readResourceFile("account/put-account-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .body(request)
                .put(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all();
    }


    @ParameterizedTest
    @MethodSource("putAccountBadRequestSource")
    @DisplayName("PUT v1/account returns bad request when fields are invalid")
    @Sql(value = "/sql/account/init_one_account.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "/sql/account/clean_accounts.sql", executionPhase = AFTER_TEST_METHOD)
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("account/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("account/%s".formatted(responseFile));

        var response = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .put(URL)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> putAccountBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-account-empty-fields-400.json", "put-response-account-empty-fields-400.json"),
                Arguments.of("put-request-account-blank-fields-400.json", "put-response-account-blank-fields-400.json"),
                Arguments.of("put-request-account-invalid-email-400.json", "put-response-account-invalid-email-400.json")
        );
    }
    */






}