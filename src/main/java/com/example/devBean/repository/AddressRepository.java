package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Address;

/**
 * The repository in Spring Boot is the data access layer that allows us to interact with our real database for operations like insert, update, and delete using Spring Data JPA
 */

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
