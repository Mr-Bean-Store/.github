package com.example.devBean.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.random.RandomGenerator;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.OrderModelAssembler;
import com.example.devBean.exception.OrderNotFoundException;
import com.example.devBean.model.Order;
import com.example.devBean.model.OrderItem;
import com.example.devBean.model.Price;
import com.example.devBean.model.Product;
import com.example.devBean.repository.OrderRepository;
import com.example.devBean.repository.ProductRepository;

import java.util.Date;

@RestController
public class OrderController {
    
    private final OrderRepository repository;
    private final OrderModelAssembler assembler;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository repository, OrderModelAssembler assembler, ProductRepository productRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.productRepository = productRepository;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> allOrders() {

        List<EntityModel<Order>> orders = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(orders,
            linkTo(methodOn(OrderController.class).allOrders()).withSelfRel());
    }

    @GetMapping("/order/{id}")
    public EntityModel<Order> oneOrder(@PathVariable Long id) {

        Order order = repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/order")
    ResponseEntity<?> newOrder(@RequestBody Order newOrder) throws URISyntaxException {
        EntityModel<Order> entityModel = assembler.toModel(repository.save(newOrder));
        return ResponseEntity // ResponseEntity is necessary because we want a more detailed HTTP response code than 200 OK
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) // URI -> uniform resource identifier | URL -> uniform resource locator
            .body(entityModel);
    }

    @PostMapping("/createOrderFromProducts")
    ResponseEntity<?> newOrderFromProducts(@RequestBody List<Long> productIds) throws URISyntaxException {
        // Create a new order with orderDate and arrivalDate
        LocalDate currentDate = LocalDate.now();
        Order newOrder = new Order(currentDate.toString(), currentDate.plusDays(10).toString());
        // You can adjust the orderDate and arrivalDate logic based on your requirements

        // Save the order to the repository
        Order savedOrder = repository.save(newOrder);

        // Retrieve products from the database using the provided IDs
        List<Product> products = productRepository.findAllById(productIds);

        // Associate the products with the order (assuming OrderItem contains product details)
        for (Product product : products) {
            OrderItem orderItem = new OrderItem();
            // Set other details of the order item as needed
            orderItem.setProduct(product);
            // Associate the order item with the order
            orderItem.setOrder(savedOrder);
            Random r = new Random();
            Double priceAmount = r.nextDouble(1000, 100000); // Generate a random price amount
            Price price = new Price(priceAmount, currentDate.toString());
            orderItem.setPrice(price);
        }

        // Save the updated order with associated products
        Order updatedOrder = repository.save(savedOrder);

        // Return the response
        EntityModel<Order> entityModel = assembler.toModel(updatedOrder);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/orders/{id}") 
    public ResponseEntity<?> replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) throws URISyntaxException {

        Order updatedOrder = repository.findById(id)
            .map(order -> {
                order.setCustomer(newOrder.getCustomer());
                order.setArrivalDate(newOrder.getArrivalDate());
                order.setOrderDate(newOrder.getOrderDate());
                order.setDelivery(newOrder.getDelivery()); //order.setOrderItems(newOrder.getOrderItems());
                return repository.save(order);
            })
            .orElseGet(() -> {
                newOrder.setOrderId(id);;
                return repository.save(newOrder);
            });

        EntityModel<Order> entityModel = assembler.toModel(updatedOrder);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/orders/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
