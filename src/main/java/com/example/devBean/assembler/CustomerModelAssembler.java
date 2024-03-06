package com.example.devBean.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.devBean.controller.CustomerController;
import com.example.devBean.model.Customer;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        return EntityModel.of(customer,
            linkTo(methodOn(CustomerController.class).oneCustomer(customer.getCustomerId())).withSelfRel(),
            linkTo(methodOn(CustomerController.class).allCustomers()).withRel("customers"));
    }
    
}
