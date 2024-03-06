package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
