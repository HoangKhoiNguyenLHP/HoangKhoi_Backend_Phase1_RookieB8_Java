package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.AccountAdminDto;

public interface AccountAdminService
{
    // [Post] /accounts/create
    AccountAdminDto createAccountAdmin(AccountAdminDto accountAdminDto);
}
