package com.example.devBean.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.example.devBean.controller.AddressController;
import com.example.devBean.model.Address;

@Component
public class AddressModelAssembler implements RepresentationModelAssembler<Address, EntityModel<Address>> {
    
    @Override
    public EntityModel<Address> toModel(Address a) {
        return EntityModel.of(a,
            linkTo(methodOn(AddressController.class).oneAddress(a.getAddressId())).withSelfRel(),
            linkTo(methodOn(AddressController.class).allAddresses()).withRel("addresses"));
    }
}
