package com.example.devBean.controller;

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

import com.example.devBean.assembler.OrderItemModelAssembler;
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
    public ResponseEntity<?> oneOrderItem(@PathVariable Long id) {

        Optional<OrderItem> orderItem = repository.findById(id);
        if (orderItem.isPresent()) {
            EntityModel<OrderItem> orderItemModel = assembler.toModel(orderItem.get());
            return ResponseEntity.ok(orderItemModel);
        }
        String errorMessage = "OrderItem not found with id: " + id;
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }
}
