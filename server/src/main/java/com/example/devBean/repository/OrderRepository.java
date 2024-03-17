package com.example.devBean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.devBean.model.Customer;
import com.example.devBean.model.Order;
import com.example.devBean.model.OrderItem;
import com.example.devBean.repository.OrderItemRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}
