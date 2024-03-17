package com.example.devBean.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.ProductModelAssembler;
import com.example.devBean.model.Product;
import com.example.devBean.repository.ProductRepository;

@RestController
public class ProductController {
    
    private final ProductRepository repository;
    private final ProductModelAssembler assembler;

    
    public ProductController(ProductRepository repository, ProductModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // get all Products in system
    @GetMapping("/products")
    public CollectionModel<EntityModel<Product>> allProducts() {
        List<EntityModel<Product>> Products = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(Products);
    }

    // get one Product of the specified id in the system
    @GetMapping("/products/{id}")
    public ResponseEntity<?> oneProduct(@PathVariable Long id) {

        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            EntityModel<Product> entityModel = assembler.toModel(product.get());
            return ResponseEntity.ok(entityModel);
        }

        String errorMessage = "Customer not found with id: " + id;
        return ResponseEntity.status(HttpStatus.OK).body(createEntity("message", errorMessage));
    }

    public EntityModel<HashMap<String, String>> createEntity(String x, String y) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(x, y);
        EntityModel<HashMap<String, String>> entityModel = EntityModel.of(map);
        return entityModel;
    }
}
