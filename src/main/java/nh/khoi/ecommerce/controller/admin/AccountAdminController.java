package nh.khoi.ecommerce.controller.admin;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.request.LoginRequest;
import nh.khoi.ecommerce.request.RegisterRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid RegisterRequest accountAdminDto,
            BindingResult bindingResult
    )
    {
        try {
            if(bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldError().getDefaultMessage();
                ApiResponse<Void> response = new ApiResponse<>(400, errorMessage, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

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
        catch (RuntimeException e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                409,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // [POST] /admin/account/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginAccount(
            @RequestBody @Valid LoginRequest accountDto,
            HttpServletResponse httpResponse,
            BindingResult bindingResult
    )
    {
        try {
            if(bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldError().getDefaultMessage();
                ApiResponse<Map<String, Object>> response = new ApiResponse<>(400, errorMessage, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

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
        catch (RuntimeException e) {
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>(
                    401,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
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
}
