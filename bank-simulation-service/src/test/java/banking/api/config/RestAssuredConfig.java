package banking.api.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static banking.api.commons.Constants.*;


@TestConfiguration
@Lazy
public class RestAssuredConfig {

    @LocalServerPort
    int port;


    @Bean(name = "requestSpecificationRegularAccount")
    public RequestSpecification requestSpecificationRegularAccount() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().basic(REGULAR_USERNAME, PASSWORD);
    }

    @Bean(name = "requestSpecificationAdminAccount")
    public RequestSpecification requestSpecificationAdminAccount() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                .auth().basic(ADMIN_USERNAME, PASSWORD);
    }

}
