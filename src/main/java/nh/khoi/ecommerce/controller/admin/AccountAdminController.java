package nh.khoi.ecommerce.controller.admin;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.request.LoginRequest;
import nh.khoi.ecommerce.request.RegisterRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${pathAdmin}/account")
public class AccountAdminController
{
    private final AccountAdminService accountAdminService;

    // public AccountAdminController(AccountAdminService accountAdminService) {
    //     this.accountAdminService = accountAdminService;
    // }

    // [POST] /admin/account/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerAccount(
            @RequestBody @Valid RegisterRequest accountAdminDto
    )
    {
            accountAdminService.registerAccount(
                accountAdminDto.getFirstName(),
                accountAdminDto.getLastName(),
                accountAdminDto.getEmail(),
                accountAdminDto.getPassword()
            );

            ApiResponse<Void> response = new ApiResponse<>(
                201,
                "Register account successfully!",
                null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // [POST] /admin/account/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginAccount(
            @RequestBody @Valid LoginRequest accountDto,
            HttpServletResponse httpResponse
    )
    {
            Map<String, Object> resultFromService = accountAdminService.loginAccount(
                    accountDto.getEmail(),
                    accountDto.getPassword(),
                    httpResponse
            );

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    200,
                    "Login successfully!",
                    resultFromService
            );
            return ResponseEntity.ok(response);
    }

    // [POST] /admin/account/logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutAccount(HttpServletResponse httpResponse) {
        accountAdminService.logoutAccount(httpResponse);

        ApiResponse<Void> response = new ApiResponse<>(
                200,
                "Logout successfully!",
                null
        );
        return ResponseEntity.ok(response);
    }

    // [GET] /admin/account/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentAccount() {
        Map<String, Object> accountAdminInfo = accountAdminService.getCurrentAccount();

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                200,
                "Fetch current account admin info successfully!",
                accountAdminInfo
        );

        return ResponseEntity.ok(response);
    }
}
