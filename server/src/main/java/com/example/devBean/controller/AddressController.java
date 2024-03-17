package com.example.devBean.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.AddressModelAssembler;
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
        return CollectionModel.of(addresses);
    }

    @PostMapping("/address")
    ResponseEntity<?> newAddress(@RequestBody Address address) throws URISyntaxException {
        EntityModel<Address> entityModel = EntityModel.of(repository.save(address));
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<?> oneAddress(@PathVariable Long id) {

        Optional<Address> address = repository.findById(id);
        if (address.isPresent()) {
            EntityModel<Address> entityModel = EntityModel.of(address.get());
            return ResponseEntity.ok(entityModel);
        }
        String errorMessage = "Address not found with id: " + id;
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> replaceAddress(@RequestBody Address newAddress, @PathVariable Long id) {

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
        return ResponseEntity.ok(entityModel);
    }
}
