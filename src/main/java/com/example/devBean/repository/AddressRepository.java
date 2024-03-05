package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Address;

interface AddressRepository extends JpaRepository<Address, Long> {
    
}
