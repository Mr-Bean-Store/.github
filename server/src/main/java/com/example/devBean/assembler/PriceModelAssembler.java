package com.example.devBean.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.example.devBean.model.Price;

@Component
public class PriceModelAssembler implements RepresentationModelAssembler<Price, EntityModel<Price>> {
    
    @Override
    public EntityModel<Price> toModel(Price price) {
        return EntityModel.of(price);
    }
}
