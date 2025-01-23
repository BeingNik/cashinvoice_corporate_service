package com.example.camel_sql.repository;
import com.example.camel_sql.entity.RouteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteConfigRepository extends JpaRepository<RouteConfig, Integer> {
}
