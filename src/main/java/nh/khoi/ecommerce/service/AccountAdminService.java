package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.AccountAdminDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AccountAdminService
{
    // [GET] /accounts
    List<AccountAdminDto> getAllAccountsAdmin();

    // [GET] /accounts/:id
    AccountAdminDto getAccountAdminById(UUID accountAdminId);

    // [Post] /accounts
    AccountAdminDto createAccountAdmin(AccountAdminDto accountAdminDto);

    // [PATCH] /accounts/:id
    AccountAdminDto editAccountAdmin(Map<String, Object> updateFields, UUID accountAdminId);

    // [DELETE] /accounts/:id
    void deleteAccountAdmin(UUID employeeId);
}
