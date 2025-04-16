package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.AccountAdminDto;

import java.util.List;
import java.util.UUID;

public interface AccountAdminService
{
    // [GET] /accounts/list
    List<AccountAdminDto> getAllAccountsAdmin();

    // [GET] /accounts/:id
    AccountAdminDto getAccountAdminById(UUID accountAdminId);

    // [Post] /accounts/create
    AccountAdminDto createAccountAdmin(AccountAdminDto accountAdminDto);
}
