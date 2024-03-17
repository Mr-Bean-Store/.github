package com.example.devBean.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import com.example.devBean.repository.CustomerRepository;
import com.example.devBean.repository.OrderRepository;
import com.example.devBean.assembler.CustomerModelAssembler;
import com.example.devBean.model.Customer;
import com.example.devBean.model.Order;

/**
 * We have routes for each operations (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping, corresponding to HTTP GET, POST, PUT, and DELETE calls
 * The difference between PUT and POST, is that PUT is for replacing the record and therefore an id is required, and POST is for creating a new record
 * API Rules
 * 1. User signs up our platform using the CLI interface
 * 2. However, they sign up using their github account, so it's a social login 
 * 3. User selects products they want to buy (user can choose buy 10 identical products), so that means a User can have 1 order item
 * 4. Customer can have multiple orders, and each order can have multiple order items. Each product can be in multiple order items and orders
 * 5. 
 *  
 */

@RestController
public class CustomerController {

    private final CustomerRepository repository;
    private final CustomerModelAssembler assembler;
    private final OrderRepository orderRepository;
    
    public CustomerController(CustomerRepository repository, CustomerModelAssembler assembler, OrderRepository order) {
        this.repository = repository;
        this.assembler = assembler;
        this.orderRepository = order;
    }

    // get all customers in system
    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> allCustomers() {
        List<EntityModel<Customer>> customers = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());
        
        CollectionModel<EntityModel<Customer>> response = CollectionModel.of(customers);

        return response;
    }

    @PostMapping("/customer")
    ResponseEntity<?> newCustomer(@RequestBody Customer newCustomer) throws URISyntaxException {
        try {
            EntityModel<Customer> entityModel = assembler.toModel((Customer)this.repository.save(newCustomer));
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        } 
        catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintEx = (ConstraintViolationException) ex.getCause();
                String constraintName = constraintEx.getConstraintName();
                if (constraintName != null && constraintName.startsWith("customers_email_key")) {
                    return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Duplicate entry. Please provide unique data.");
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }  
    }

    // get one customer of the specified id in the system
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> oneCustomer(@PathVariable Long id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isPresent()) {
            EntityModel<Customer> entityModel = assembler.toModel(customer.get());
            return ResponseEntity.ok(entityModel);
        }
        String errorMessage = "Customer not found with id: " + id;
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    @PutMapping("/customers/{id}") // replaces existing customer with a new customer
    public ResponseEntity<?> replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) throws URISyntaxException {
        Customer updatedCustomer = repository.findById(id)
            .map(customer -> {
                customer.setFirstName(newCustomer.getFirstName());
                customer.setLastName(newCustomer.getLastName());
                return repository.save(customer);
            })
            .orElseGet(() -> {
                newCustomer.setCustomerId(id);
                return repository.save(newCustomer);
            });

        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/customer-by-email/{email}")
    public ResponseEntity<?> checkCustomer(@PathVariable String email) {
        Optional<Customer> customer = repository.findByEmail(email);
        if (customer.isPresent()) {
            EntityModel<Customer> entityModel = assembler.toModel(customer.get());
            return ResponseEntity.ok(entityModel);
        }
        String errorMessage = "Customer not found with email: " + email;
        return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
    }

    @DeleteMapping({"/customers/{id}"})
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isPresent()) {
            List<Order> orders = orderRepository.findByCustomer(customer.get());
            if (orders.size() > 0) {
                String message = "Customer still has valid orders";
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            }
            this.repository.deleteById(id);
            String message = "Customer deleted from system";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        String message = "Customer id is invalid.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
