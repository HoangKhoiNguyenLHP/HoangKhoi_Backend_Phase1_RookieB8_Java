package nh.khoi.ecommerce.service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AccountAdminService
{
    // [POST] /admin/account/register
    void registerAccount(String firstName, String lastName, String email, String password);

    // [POST] /admin/account/login
    Map<String, Object> loginAccount(String email, String password, HttpServletResponse response);

    // [POST] /admin/account/logout
    void logoutAccount(HttpServletResponse response);

    // [GET] /admin/account/profile
    Map<String, Object> getCurrentAccount();
}
