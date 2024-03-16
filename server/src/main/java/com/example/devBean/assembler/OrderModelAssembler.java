package com.example.devBean.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.devBean.model.Order;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    
    @Override
    public EntityModel<Order> toModel(Order item) {
        return EntityModel.of(item);
    }
}