package com.example.devBean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.devBean.model.ProductModel;
import java.util.List;
import java.util.Optional;


public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {
    Optional<ProductModel> findModelByDescription(String description);
}
