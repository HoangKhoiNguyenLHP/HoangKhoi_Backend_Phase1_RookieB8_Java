package nh.khoi.ecommerce.config;

import nh.khoi.ecommerce.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Value("${pathAdmin}")
    private String adminPath;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
                .requestMatchers("/" + adminPath + "/account/login").permitAll()

                // 🔒 Secure all other admin paths
                .requestMatchers("/" + adminPath + "/**").authenticated()

                // ✅ Permit everything else (public routes like /products, /tour, etc.)
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}