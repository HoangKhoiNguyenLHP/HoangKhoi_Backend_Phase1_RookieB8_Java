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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                .requestMatchers("/" + adminPath + "/account/logout").permitAll()

                // 🔒 Secure all other admin paths
                .requestMatchers("/" + adminPath + "/**").authenticated()

                // ✅ Permit everything else (public routes like /products, /tour, etc.)
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        // ----- CORS ----- //
        CorsConfiguration config = new CorsConfiguration();

        // --- Cach 1 : tat ca cac ten mien khac duoc phep truy cap vao tat ca API cua minh
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        // --- End cach 1 : tat ca cac ten mien khac duoc phep truy cap vao tat ca API cua minh

        // --- Cach 2 : chi co dung ten mien moi duoc phep truy cap vao tat ca API cua minh
        // config.setAllowedOrigins(Arrays.asList("http://abc.com", "http://xyz.com"));
        // --- End cach 2 : chi co dung ten mien moi duoc phep truy cap vao tat ca API cua minh

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        config.setAllowCredentials(true); // allow sending cookie, JWT through header
        // ----- End CORS ----- //

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // apply for all routes
        return source;
    }
}