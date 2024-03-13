package com.example.devBean.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

//import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import com.example.devBean.repository.CustomerRepository;
import com.example.devBean.assembler.CustomerModelAssembler;
import com.example.devBean.exception.CustomerNotFoundException;
import com.example.devBean.model.Customer;

/**
 * We have routes for each operations (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping, corresponding to HTTP GET, POST, PUT, and DELETE calls
 * The difference between PUT and POST, is that PUT is for replacing the record and therefore an id is required, and POST is for creating a new record
 * API Rules
 * 1. User signs up our platform using the CLI interface
 * 2. However, they sign up using their google account, so it's a social login 
 * 3. User selects products they want to buy (user can choose buy 10 identical products), so that means a User can have 1 order item
 * 4. Customer can have multiple orders, and each order can have multiple order items. Each order item can have multiple products.
 * 5. 
 *  
 */

@RestController
public class CustomerController {

    private final CustomerRepository repository;
    private final CustomerModelAssembler assembler;

    
    CustomerController(CustomerRepository repository, CustomerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // get all customers in system
    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> allCustomers() {

        List<EntityModel<Customer>> customers = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(customers,
            linkTo(methodOn(CustomerController.class).allCustomers()).withSelfRel());
    }

    // create a new customer account, this function will save the customers details
    @PostMapping("/customer")
    ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer) throws URISyntaxException {
        EntityModel<Customer> entityModel = assembler.toModel(repository.save(newCustomer));
        return ResponseEntity // ResponseEntity is necessary because we want a more detailed HTTP response code than 200 OK
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // URI -> uniform resource identifier | URL -> uniform resource locator
            .body(entityModel);
    }

    // get one customer of the specified id in the system
    @GetMapping("/customers/{id}")
    public EntityModel<Customer> oneCustomer(@PathVariable Long id) {

        Customer customer = repository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));

        return assembler.toModel(customer);
    }

    @PutMapping("/customers/{id}") // replaces existing customer with a new customer
    public ResponseEntity<?> replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) throws URISyntaxException {

        Customer updatedCustomer = repository.findById(id)
            .map(customer -> {
                customer.setEmail(newCustomer.getEmail());
                customer.setFirstName(newCustomer.getFirstName());
                customer.setLastName(newCustomer.getLastName());
                customer.setEmail(newCustomer.getEmail());
                customer.setOrders(newCustomer.getOrders());
                return repository.save(customer);
            })
            .orElseGet(() -> {
                newCustomer.setCustomerId(id);
                return repository.save(newCustomer);
            });

        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
