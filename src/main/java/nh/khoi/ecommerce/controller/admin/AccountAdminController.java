package nh.khoi.ecommerce.controller.admin;


import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.request.RegisterRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Void>> registerAccount(@RequestBody RegisterRequest accountAdminDto) {
        try {
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
}
