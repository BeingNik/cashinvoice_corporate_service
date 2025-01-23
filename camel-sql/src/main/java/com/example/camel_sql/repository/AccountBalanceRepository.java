package com.example.camel_sql.repository;

import com.example.camel_sql.entity.AccountBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBalanceRepository extends JpaRepository<AccountBalanceEntity, String> {

    Optional<AccountBalanceEntity> findByAccountIdentification(String accountIdentification);
}
