package com.example.devBean.assembler;
import org.springframework.stereotype.Component;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.example.devBean.model.Address;

@Component
public class AddressModelAssembler implements RepresentationModelAssembler<Address, EntityModel<Address>> {
    
    @Override
    public EntityModel<Address> toModel(Address a) {
        return EntityModel.of(a);
    }
}
