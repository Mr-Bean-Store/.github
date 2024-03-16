package com.example.devBean.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.devBean.model.Order;
import com.example.devBean.model.OrderItem;
import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
