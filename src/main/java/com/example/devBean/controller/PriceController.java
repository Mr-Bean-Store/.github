package com.example.devBean.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.devBean.assembler.PriceModelAssembler;
import com.example.devBean.exception.PriceNotFoundException;
import com.example.devBean.model.Price;
import com.example.devBean.repository.PriceRepository;

public class PriceController {
    
    private final PriceRepository repository;
    private final PriceModelAssembler assembler;

    PriceController(PriceRepository repository, PriceModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/prices")
    public CollectionModel<EntityModel<Price>> allPrices() {

        List<EntityModel<Price>> prices = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(prices,
            linkTo(methodOn(PriceController.class).allPrices()).withSelfRel());
    }

    @PostMapping("/price")
    ResponseEntity<?> newPrice(@RequestBody Price newPrice) throws URISyntaxException {
        EntityModel<Price> entityModel = assembler.toModel(repository.save(newPrice));
        return ResponseEntity // ResponseEntity is necessary because we want a more detailed HTTP response code than 200 OK
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // URI -> uniform resource identifier | URL -> uniform resource locator
            .body(entityModel);
    }

    @GetMapping("/prices/{id}")
    public EntityModel<Price> onePrice(@PathVariable Long id) {

        Price price = repository.findById(id)
            .orElseThrow(() -> new PriceNotFoundException(id));

        return assembler.toModel(price);
    }

    @PutMapping("/prices/{id}") // replaces existing customer with a new customer
    public ResponseEntity<?> replaceCustomer(@RequestBody Price newPrice, @PathVariable Long id) throws URISyntaxException {

        Price updatedPrice = repository.findById(id)
            .map(price -> {
                price.setDate(newPrice.getDate());
                price.setAmount(newPrice.getAmount());
                price.setModel(newPrice.getModel());
                price.setItems(newPrice.getItems());
                return repository.save(price);
            })
            .orElseGet(() -> {
                newPrice.setPriceId(id);
                return repository.save(newPrice);
            });

        EntityModel<Price> entityModel = assembler.toModel(updatedPrice);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }
}
