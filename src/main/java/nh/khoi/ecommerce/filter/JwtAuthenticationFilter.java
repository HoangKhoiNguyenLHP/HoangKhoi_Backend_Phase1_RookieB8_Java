package nh.khoi.ecommerce.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.repository.AccountAdminRepository;
import nh.khoi.ecommerce.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    @Value("${pathAdmin}")
    private String adminPath;

    @Autowired
    private AccountAdminRepository accountAdminRepository;

    private void writeErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException
    {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Void> errorResponse = new ApiResponse<>(statusCode, message, null);

        ObjectMapper mapper = new ObjectMapper();
        String responseBody = mapper.writeValueAsString(errorResponse);

        response.getWriter().write(responseBody);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.equals("/" + adminPath + "/account/login") ||
                path.equals("/" + adminPath + "/account/register") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        // ----- Get token from cookie (Postman, SwaggerUI) ----- //
        String token = null;

        // try reading from Authorization header first
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer "
        }

        // fallback to cookie if header is not present
        // if (token == null && request.getCookies() != null) {
        //     for (Cookie cookie : request.getCookies()) {
        //         if ("token".equals(cookie.getName())) {
        //             token = cookie.getValue();
        //             break;
        //         }
        //     }
        // }
        // ----- End get token from cookie (Postman, SwaggerUI) ----- //


        // ----- Check if there is token ----- //
        if (token == null) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Please send with token!");
            return;
        }
        // ----- End check if there is token ----- //

        try {
            // if wrong token, stuck the server
            // use try catch
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            // after successfully decode jwt, get email, get userId
            String email = claims.get("email", String.class);
            UUID id = UUID.fromString(claims.get("id", String.class));

            Optional<AccountAdmin> isAccountExist = accountAdminRepository.findByIdAndEmailAndStatus(id, email, "active");

            if(isAccountExist.isEmpty()) {
                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Token not valid!");
                return;
            }

            AccountAdmin foundAdmin = isAccountExist.get();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            foundAdmin, // principal
                            null,       // credentials
                            List.of()   // authorities (can customize if needed)
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e) {
            writeErrorResponse(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token is invalid or expired!"
            );
            return;
        }

        // if pass, allow access
        filterChain.doFilter(request, response);
    }
}