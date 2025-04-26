package nh.khoi.ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
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
    public Map<String, Object> loginAccount(String email, String password)
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

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", token);
        result.put("expireAt", expiryDate); // millisecond

        return result;
    }
}
