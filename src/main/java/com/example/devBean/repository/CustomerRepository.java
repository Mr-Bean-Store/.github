package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Customer;

/**
 * This Repository class is for the customer model to perform database operations
 */
interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
