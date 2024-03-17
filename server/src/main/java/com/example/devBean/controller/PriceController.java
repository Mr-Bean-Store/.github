package com.example.devBean.controller;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
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

import com.example.devBean.assembler.PriceModelAssembler;
import com.example.devBean.model.Price;
import com.example.devBean.repository.PriceRepository;

@RestController
public class PriceController {
    
    private final PriceRepository repository;
    private final PriceModelAssembler assembler;

    public PriceController(PriceRepository repository, PriceModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/prices")
    public CollectionModel<EntityModel<Price>> allPrices() {
        List<EntityModel<Price>> prices = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(prices);
    }

    @GetMapping("/prices/{id}")
    public ResponseEntity<?> onePrice(@PathVariable Long id) {
        Optional<Price> price = repository.findById(id);
        if (price.isPresent()) {
            EntityModel<Price> entityModel = assembler.toModel(price.get());
            return ResponseEntity.ok(entityModel);
        }
        String errorMessage = "Price not found with id: " + id;
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }
}
