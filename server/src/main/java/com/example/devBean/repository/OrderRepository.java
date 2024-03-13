package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
