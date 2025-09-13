package banking.api.config;

import lombok.extern.log4j.Log4j2;
//import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST = {"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cross Site Request Forgery
        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "v1/account").permitAll()
                        //.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()             // To enable actuators later. ex: prometheus
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }


}
