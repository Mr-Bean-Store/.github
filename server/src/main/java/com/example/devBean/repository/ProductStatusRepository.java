package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.ProductStatus;

public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
    
}
