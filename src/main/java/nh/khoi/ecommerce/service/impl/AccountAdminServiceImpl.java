package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.repository.AccountAdminRepository;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService
{
    private final AccountAdminRepository accountAdminRepository;
    private final BCryptPasswordEncoder passwordEncoder;


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
}
