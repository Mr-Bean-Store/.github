package com.example.devBean.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.example.devBean.model.OrderItem;

@Component
public class OrderItemModelAssembler implements RepresentationModelAssembler<OrderItem, EntityModel<OrderItem>> {
    
    @Override
    public EntityModel<OrderItem> toModel(OrderItem item) {
        return EntityModel.of(item);
    }
}
