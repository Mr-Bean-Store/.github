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

    // create a new Product account, this function will save the Products details
    @PostMapping("/product")
    ResponseEntity<?> newProduct(@RequestBody Product newProduct) throws URISyntaxException {
        EntityModel<Product> entityModel = assembler.toModel(repository.save(newProduct));
        return ResponseEntity // ResponseEntity is necessary because we want a more detailed HTTP response code than 200 OK
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // URI -> uniform resource identifier | URL -> uniform resource locator
            .body(entityModel);
    }

    // get one Product of the specified id in the system
    @GetMapping("/products/{id}")
    public EntityModel<Product> oneProduct(@PathVariable Long id) {

        Product Product = repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return assembler.toModel(Product);
    }

    @PutMapping("/products/{id}") // replaces existing Product with a new Product
    public ResponseEntity<?> replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) throws URISyntaxException {

        Product updatedProduct = repository.findById(id)
            .map(product -> {
                product.setSerialNumber(newProduct.getSerialNumber());
                product.setModel(newProduct.getModel());
                product.setItems(newProduct.getItems());
                return repository.save(product);
            })
            .orElseGet(() -> {
                newProduct.setProductId(id);
                return repository.save(newProduct);
            });

        EntityModel<Product> entityModel = assembler.toModel(updatedProduct);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
