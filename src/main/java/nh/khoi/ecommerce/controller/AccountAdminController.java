package nh.khoi.ecommerce.controller;

import lombok.AllArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
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

    // [GET] /accounts/list
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        List<AccountAdminDto> listAccountsAdmin = accountAdminService.getAllAccountsAdmin();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 200);
        responseBody.put("message", "Get list accounts admin successfully!");
        responseBody.put("data", listAccountsAdmin);

        return ResponseEntity.ok(responseBody);
    }

    // [GET] /accounts/:id
    // Test comment
    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> getAccountAdminById(@PathVariable("id") UUID accountAdminId) {
        AccountAdminDto accountAdminDto = accountAdminService.getAccountAdminById(accountAdminId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 200);
        responseBody.put("message", "Get account admin detail successfully!");
        responseBody.put("data", accountAdminDto);

        return ResponseEntity.ok(responseBody);
    }

    // [Post] /accounts/create
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAccountAdmin(@RequestBody AccountAdminDto accountAdminDto) {
        AccountAdminDto savedAccountAdmin = accountAdminService.createAccountAdmin(accountAdminDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 201);
        responseBody.put("message", "Create account admin successfully!");
        responseBody.put("data", savedAccountAdmin);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
