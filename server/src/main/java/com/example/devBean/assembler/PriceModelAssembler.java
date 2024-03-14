package com.example.devBean.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.example.devBean.controller.PriceController;
import com.example.devBean.model.Price;

@Component
public class PriceModelAssembler implements RepresentationModelAssembler<Price, EntityModel<Price>> {
    
    @Override
    public EntityModel<Price> toModel(Price price) {
        return EntityModel.of(price,
            linkTo(methodOn(PriceController.class).onePrice(price.getPriceId())).withSelfRel(),
            linkTo(methodOn(PriceController.class).allPrices()).withRel("prices"));
    }
}
