package nh.khoi.ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.repository.AccountAdminRepository;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService
{
    private final AccountAdminRepository accountAdminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    // public AccountAdminServiceImpl(AccountAdminRepository accountAdminRepository, BCryptPasswordEncoder passwordEncoder) {
    //     this.accountAdminRepository = accountAdminRepository;
    //     this.passwordEncoder = passwordEncoder;
    // }

    // [POST] /admin/account/register
    @Override
    public void registerAccount(String firstName, String lastName, String email, String password)
    {
        // ----- Check existed email ----- //
        Optional<AccountAdmin> existedAccount = accountAdminRepository.findByEmail(email);
        if(existedAccount.isPresent()) {
            throw new RuntimeException("Email existed in the system!");
        }
        // ----- End check existed email ----- //

        // ----- Encrypt password using Bcrypt ----- //
        String hashedPassword = passwordEncoder.encode(password);
        // ----- End encrypt password using Bcrypt ----- //

        AccountAdmin newAccount = new AccountAdmin();
        newAccount.setFirstName(firstName);
        newAccount.setLastName(lastName);
        newAccount.setEmail(email);
        newAccount.setPassword(hashedPassword);
        newAccount.setStatus("active");

        accountAdminRepository.save(newAccount);
    }

    // [POST] /admin/account/login
    @Override
    public Map<String, Object> loginAccount(String email, String password, HttpServletResponse response)
    {
        // ----- Check existed email ----- //
        AccountAdmin account = accountAdminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not exist in the system!"));
        // ----- End check existed email ----- //

        // ----- Check input password ----- //
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new RuntimeException("Password incorrect!");
        }
        // ----- End check input password ----- //

        // ----- Check if the account is activated ----- //
        if (!"active".equals(account.getStatus())) {
            throw new RuntimeException("Account is not yet activated!");
        }
        // ----- End check if the account is activated ----- //

        // ----- JWT generation ----- //
        long expirationMillis = 1 * 24 * 60 * 60 * 1000; // 1 day
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        String token = Jwts.builder()
                .setSubject(account.getId().toString())
                .claim("id", account.getId().toString())
                .claim("email", account.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                .compact();
        // ----- End JWT generation ----- //

        // ----- Set HttpOnly cookie ----- //
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // only Server is allowed to access this cookie
        cookie.setSecure(true); // only on HTTPS
        cookie.setPath("/"); // root path
        cookie.setMaxAge((int)(expirationMillis / 1000)); // in seconds
        cookie.setAttribute("SameSite", "Strict"); // other websites cannot use this cookie to send request
                                                              // for example, if we use FE to send this cookie, it cannot send
                                                              //              this cookie can only be automatically saved and sent by BE

        response.addCookie(cookie);
        // ----- End set cookie ----- //

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", token);
        result.put("expireAt", expiryDate); // millisecond

        return result;
    }

    // [POST] /admin/account/logout
    @Override
    public void logoutAccount(HttpServletResponse response)
    {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true); // only Server is allowed to access this cookie
        cookie.setSecure(true); // only on HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // expire immediately
        cookie.setAttribute("SameSite", "Strict"); // other websites cannot use this cookie to send request
                                                              // for example, if we use FE to send this cookie, it cannot send
                                                              //              this cookie can only be automatically saved and sent by BE

        response.addCookie(cookie);
    }
}
