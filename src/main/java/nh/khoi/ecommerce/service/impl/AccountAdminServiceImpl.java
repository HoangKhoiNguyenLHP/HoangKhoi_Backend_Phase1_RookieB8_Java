package nh.khoi.ecommerce.service.impl;

import lombok.AllArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.exception.ResourceNotFoundException;
import nh.khoi.ecommerce.mapper.AccountAdminMappper;
import nh.khoi.ecommerce.repository.AccountAdminRepository;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountAdminServiceImpl implements AccountAdminService
{
    private AccountAdminRepository accountAdminRepository;

    // [GET] /accounts
    @Override
    public List<AccountAdminDto> getAllAccountsAdmin()
    {
        List<AccountAdmin> listAccountsAdmin = accountAdminRepository.findAll();
        return listAccountsAdmin.stream()
                .map((eachAccount) -> AccountAdminMappper.mapToAccountAdminDto(eachAccount))
                .collect(Collectors.toList());
    }

    // [GET] /accounts/:id
    @Override
    public AccountAdminDto getAccountAdminById(UUID accountAdminId)
    {
        AccountAdmin accountAdmin = accountAdminRepository.findById(accountAdminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account does not exist with given id: " + accountAdminId
                ));

        return AccountAdminMappper.mapToAccountAdminDto(accountAdmin);
    }

    // [Post] /accounts
    @Override
    public AccountAdminDto createAccountAdmin(AccountAdminDto accountAdminDto)
    {
        AccountAdmin accountAdmin = AccountAdminMappper.mapToAccountAdmin(accountAdminDto);
        AccountAdmin savedAccountAdmin = accountAdminRepository.save(accountAdmin);

        return AccountAdminMappper.mapToAccountAdminDto(savedAccountAdmin);
    }

    // [PATCH] /accounts/:id
    @Override
    public AccountAdminDto editAccountAdmin(Map<String, Object> updateFields, UUID accountAdminId)
    {
        AccountAdmin accountAdmin = accountAdminRepository.findById(accountAdminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account does not exist with given id: " + accountAdminId
                ));

        updateFields.forEach((key, value) -> {
            switch(key) {
                case "firstName":
                    accountAdmin.setFirstName((String)value);
                    break;
                case "lastName":
                    accountAdmin.setLastName((String) value);
                    break;
                case "email":
                    accountAdmin.setEmail((String) value);
                    break;
            }
        });

        AccountAdmin updatedAccount = accountAdminRepository.save(accountAdmin);
        return AccountAdminMappper.mapToAccountAdminDto(updatedAccount);
    }

    // [DELETE] /accounts/:id
    @Override
    public void deleteAccountAdmin(UUID accountAdminId)
    {
        AccountAdmin accountAdmin = accountAdminRepository.findById(accountAdminId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account does not exist with given id: " + accountAdminId
                ));

        accountAdminRepository.deleteById(accountAdminId);
    }
}
