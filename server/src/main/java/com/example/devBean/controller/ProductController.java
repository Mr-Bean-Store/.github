package com.example.devBean.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.ProductModelAssembler;
import com.example.devBean.exception.ProductNotFoundException;
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

        return CollectionModel.of(Products,
            linkTo(methodOn(ProductController.class).allProducts()).withSelfRel());
    }

    // get one Product of the specified id in the system
    @GetMapping("/products/{id}")
    public EntityModel<Product> oneProduct(@PathVariable Long id) {

        Product Product = repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return assembler.toModel(Product);
    }
}
