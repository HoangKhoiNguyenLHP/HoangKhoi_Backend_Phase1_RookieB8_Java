package nh.khoi.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Value("${pathAdmin}")
    private String adminPath;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for Postman and Swagger testing
            .authorizeHttpRequests(auth -> auth
                // Allow Swagger UI
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ✅ Allow access to admin registration and login
                .requestMatchers("/" + adminPath + "/account/register").permitAll()


                // ✅ Permit everything else (public routes like /products, /tour, etc.)
                .anyRequest().permitAll()
            );
        return http.build();
    }
}