package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.Price;
import com.example.devBean.model.ProductModel;

import java.util.List;


public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByModel(ProductModel model);
}
