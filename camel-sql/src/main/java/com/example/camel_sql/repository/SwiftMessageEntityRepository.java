package com.example.camel_sql.repository;

import com.example.camel_sql.entity.SwiftMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftMessageEntityRepository extends JpaRepository<SwiftMessageEntity, Long> {
}
