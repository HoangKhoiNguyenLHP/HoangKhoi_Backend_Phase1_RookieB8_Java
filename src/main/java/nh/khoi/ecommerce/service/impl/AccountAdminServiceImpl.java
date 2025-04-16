package nh.khoi.ecommerce.service.impl;

import lombok.AllArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.mapper.AccountAdminMappper;
import nh.khoi.ecommerce.repository.AccountAdminRepository;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService
{
    private AccountAdminRepository accountAdminRepository;

    // [Post] /accounts/create
    @Override
    public AccountAdminDto createAccountAdmin(AccountAdminDto accountAdminDto)
    {
        AccountAdmin accountAdmin = AccountAdminMappper.mapToAccountAdmin(accountAdminDto);
        AccountAdmin savedAccountAdmin = accountAdminRepository.save(accountAdmin);

        return AccountAdminMappper.mapToAccountAdminDto(savedAccountAdmin);
    }
}
