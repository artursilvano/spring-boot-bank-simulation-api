package banking.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "JWT Auth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(title = "Bank Simulation Service API", version = "v1"),
        tags = {
                @Tag(name = "Auth Controller", description = "Authentication related endpoints"),
                @Tag(name = "Account Controller", description = "Account related endpoints"),
                @Tag(name = "Transaction Controller", description = "Transaction related endpoints"),
                @Tag(name = "ATM Controller", description = "ATM related endpoints")
        }
)
public class OpenApiConfig {
}
