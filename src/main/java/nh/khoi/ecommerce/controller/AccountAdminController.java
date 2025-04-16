package nh.khoi.ecommerce.controller;

import lombok.AllArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountAdminController
{
    private AccountAdminService accountAdminService;

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
