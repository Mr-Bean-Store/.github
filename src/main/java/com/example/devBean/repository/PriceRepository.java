package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
    
}
