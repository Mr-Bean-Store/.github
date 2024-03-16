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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.OrderItemModelAssembler;
import com.example.devBean.exception.OrderItemNotFoundException;
import com.example.devBean.model.OrderItem;
import com.example.devBean.repository.OrderItemRepository;

@RestController
public class OrderItemController {
    
    private final OrderItemRepository repository;
    private final OrderItemModelAssembler assembler;

    public OrderItemController(OrderItemRepository repository, OrderItemModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // get all customers in system
    @GetMapping("/orderItems")
    public CollectionModel<EntityModel<OrderItem>> allOrderItems() {

        List<EntityModel<OrderItem>> orderItems = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(orderItems);
    }

    @GetMapping("/orderItems/{id}")
    public EntityModel<OrderItem> oneOrderItem(@PathVariable Long id) {

        OrderItem orderItem = repository.findById(id)
            .orElseThrow(() -> new OrderItemNotFoundException(id));

        return assembler.toModel(orderItem);
    }
}
