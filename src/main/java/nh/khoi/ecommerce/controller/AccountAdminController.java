package nh.khoi.ecommerce.controller;

import lombok.AllArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountAdminController
{
    private AccountAdminService accountAdminService;

    // [GET] /accounts
    @GetMapping()
    public ResponseEntity<ApiResponse<List<AccountAdminDto>>> getAllEmployees() {
        List<AccountAdminDto> listAccountsAdmin = accountAdminService.getAllAccountsAdmin();

        ApiResponse<List<AccountAdminDto>> response = new ApiResponse<>(
                200,
                "Get list admin accounts successfully!",
                listAccountsAdmin
        );
        return ResponseEntity.ok(response);
    }

    // [GET] /accounts/:id
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountAdminDto>> getAccountAdminById(@PathVariable("id") UUID accountAdminId) {
        AccountAdminDto accountAdminDto = accountAdminService.getAccountAdminById(accountAdminId);

        ApiResponse<AccountAdminDto> response = new ApiResponse<>(
                200,
                "Get account admin detail successfully!",
                accountAdminDto
        );
        return ResponseEntity.ok(response);
    }

    // [Post] /accounts
    @PostMapping()
    public ResponseEntity<ApiResponse<AccountAdminDto>> createAccountAdmin(@RequestBody AccountAdminDto accountAdminDto) {
        AccountAdminDto savedAccountAdmin = accountAdminService.createAccountAdmin(accountAdminDto);

        ApiResponse<AccountAdminDto> response = new ApiResponse<>(
                201,
                "Create account admin successfully!",
                savedAccountAdmin
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // [PATCH] /accounts/:id
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<AccountAdminDto>> editAccountAdmin(@RequestBody Map<String, Object> updateFields,
                                                                         @PathVariable("id") UUID accountAdminId) {
        AccountAdminDto updatedAccount = accountAdminService.editAccountAdmin(updateFields, accountAdminId);

        ApiResponse<AccountAdminDto> response = new ApiResponse<>(
                200,
                "Update account admin successfully!",
                updatedAccount
        );
        return ResponseEntity.ok(response);
    }

    // [DELETE] /accounts/:id
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccountAdmin(@PathVariable("id") UUID accountAdminId) {
        accountAdminService.deleteAccountAdmin(accountAdminId);

        ApiResponse<Void> response = new ApiResponse<>(
                200,
                "Delete account admin successfully!",
                null
        );
        return ResponseEntity.ok(response);
    }
}
