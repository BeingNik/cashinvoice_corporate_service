package com.example.camel_sql.service;


import com.example.camel_sql.dto.AccountBalanceResponseDto;
import com.example.camel_sql.entity.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountBalanceService {

    Response getBalances(String accountIdentification);
}

