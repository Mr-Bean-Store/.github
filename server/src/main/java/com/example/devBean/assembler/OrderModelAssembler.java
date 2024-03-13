package com.example.devBean.assembler;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.example.devBean.controller.ProductController;
import com.example.devBean.model.Order;

public class OrderModelAssembler {//implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    
    /*@Override
    public EntityModel<Order> toModel(Order item) {
        return EntityModel.of(item,
            linkTo(methodOn(OrderController.class).oneOrder(item.getOrderId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).allOrders()).withRel("orders"));
    }*/
}