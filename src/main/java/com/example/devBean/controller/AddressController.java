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

import com.example.devBean.assembler.AddressModelAssembler;
import com.example.devBean.exception.AddressNotFoundException;
import com.example.devBean.model.Address;
import com.example.devBean.repository.AddressRepository;

@RestController
public class AddressController {
    
    private final AddressRepository repository;
    private final AddressModelAssembler assembler;

    public AddressController(AddressRepository repository, AddressModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/addresses")
    public CollectionModel<EntityModel<Address>> allAddresses() {

        List<EntityModel<Address>> addresses = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(addresses,
            linkTo(methodOn(AddressController.class).allAddresses()).withSelfRel());
    }

    @GetMapping("/addresses/{id}")
    public EntityModel<Address> oneAddress(@PathVariable Long id) {

        Address address = repository.findById(id)
            .orElseThrow(() -> new AddressNotFoundException(id));

        return assembler.toModel(address);
    }

    @PutMapping("/addresses/{id}") // replaces existing customer with a new customer
    public ResponseEntity<?> replaceAddress(@RequestBody Address newAddress, @PathVariable Long id) throws URISyntaxException {

        Address updatedAddress = repository.findById(id)
            .map(address -> {
                address.setLatitude(newAddress.getLatitude());
                address.setLongitude(newAddress.getLongitude());
                return repository.save(address);
            })
            .orElseGet(() -> {
                newAddress.setAddressId(id);
                return repository.save(newAddress);
            });

        EntityModel<Address> entityModel = assembler.toModel(updatedAddress);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/addresses/{id}")
    ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
