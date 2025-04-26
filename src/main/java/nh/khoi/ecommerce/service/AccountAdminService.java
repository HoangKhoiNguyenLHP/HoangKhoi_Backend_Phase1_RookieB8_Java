package nh.khoi.ecommerce.service;

import java.util.Map;

public interface AccountAdminService
{
    // [POST] /admin/account/register
    void registerAccount(String firstName, String lastName, String email, String password);
}
