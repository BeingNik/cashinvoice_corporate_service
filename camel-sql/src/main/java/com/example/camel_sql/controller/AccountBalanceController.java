package com.example.camel_sql.controller;


import com.example.camel_sql.dto.AccountBalanceResponseDto;
import com.example.camel_sql.entity.Response;
import com.example.camel_sql.service.AccountBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balance")
@CrossOrigin()
public class AccountBalanceController {
    @Autowired
    private AccountBalanceService accountBalanceService;


    @GetMapping("/check")
    public Response checkBalances(@RequestParam("accountIdentification") String accountIdentification) {
        return accountBalanceService.getBalances(accountIdentification);
    }

}
