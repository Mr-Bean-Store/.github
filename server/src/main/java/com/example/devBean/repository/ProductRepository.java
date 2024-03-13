package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
