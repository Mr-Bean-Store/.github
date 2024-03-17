package com.example.devBean.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.devBean.assembler.OrderModelAssembler;
import com.example.devBean.model.Address;
import com.example.devBean.model.Customer;
import com.example.devBean.model.Order;
import com.example.devBean.model.OrderItem;
import com.example.devBean.model.Price;
import com.example.devBean.model.Product;
import com.example.devBean.model.ProductModel;
import com.example.devBean.repository.AddressRepository;
import com.example.devBean.repository.CustomerRepository;
import com.example.devBean.repository.OrderItemRepository;
import com.example.devBean.repository.OrderRepository;
import com.example.devBean.repository.ProductModelRepository;
import com.example.devBean.repository.ProductRepository;
import com.example.devBean.repository.PriceRepository;

@RestController
public class OrderController {
    
    private final OrderRepository repository;
    private final OrderModelAssembler assembler;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final PriceRepository priceRepository;

    public OrderController(OrderRepository repository, OrderModelAssembler assembler, ProductRepository productRepository, CustomerRepository customerRepository, AddressRepository addressRepository, OrderItemRepository orderItemRepository, ProductModelRepository productModelRepository, PriceRepository priceRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.priceRepository = priceRepository;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> allOrders() {
        List<EntityModel<Order>> orders = repository.findAll().stream()
        .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(orders);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> oneOrder(@PathVariable Long id) {

        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            Order o = order.get();
            EntityModel<Order> entityModel = assembler.toModel(o);
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        }
        String errorMessage = "Order not found with id: " + id;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createEntity("message", errorMessage));
    }

    @GetMapping("/order-price/{id}")
    public ResponseEntity<?> orderPrice(@PathVariable Long id) {

        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            Order o = order.get();
            List<OrderItem> orderItems = orderItemRepository.findByOrder(o);
            Double totalPrice = orderItems.stream().mapToDouble(item -> item.getPrice().getAmount()).sum();
            String t = Double.toString(totalPrice);
            return ResponseEntity.status(HttpStatus.OK).body(createEntity("price", t));
        }
        String errorMessage = "Order not found with id: " + id;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createEntity("message", errorMessage));
    }

    @PostMapping("/order")
    ResponseEntity<?> newOrder(@RequestBody Order newOrder) {
        EntityModel<Order> entityModel = assembler.toModel(repository.save(newOrder));
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/create-order")
    ResponseEntity<?> newOrderFromProducts( @RequestParam Long customerId,
                                            @RequestParam Long addressId,
                                            @RequestBody List<Long> productIds) {

        LocalDate currentDate = LocalDate.now();
        Timestamp orderDate = Timestamp.valueOf(currentDate.atStartOfDay());
        Timestamp arrivalDate = Timestamp.valueOf(currentDate.plusDays(10).atStartOfDay());

        Order newOrder = new Order(orderDate, arrivalDate);
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Address> address =  addressRepository.findById(addressId);

        if (customer.isPresent() && address.isPresent()) {

            newOrder.setCustomer(customer.get());
            newOrder.setDelivery(address.get());

            List<Product> products = productRepository.findAllById(productIds);
            List<OrderItem> items = new ArrayList<OrderItem>();
            
            products.stream()
            .forEach(product -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setOrder(newOrder);

                ProductModel model = product.getModel();
                List<Price> prices = priceRepository.findByModel(model);
                System.out.println(prices);
                Price price = prices.get(prices.size()-1); // this will use the latest price
                price.setModel(model);

                orderItem.setPrice(price);
                orderItemRepository.save(orderItem);

                items.add(orderItem);
            });

            Order updatedOrder = repository.save(newOrder);
            EntityModel<Order> entityModel = assembler.toModel(updatedOrder);
            return ResponseEntity.ok(entityModel);
        }
        String errorMessage = "Customer id is invalid: " + customerId + " or address id does not exist";
        return ResponseEntity.status(HttpStatus.OK).body(createEntity("message", errorMessage));
        
    }

    @GetMapping("/customer-orders/{customerId}")
    public ResponseEntity<?> getOrders(@PathVariable Long customerId) {
        List<HashMap<Long, List<Product>>> order_products = new ArrayList<>();
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Customer c = customer.get();
            List<Order> orders = repository.findByCustomer(c);
            orders.stream()
            .forEach(order -> {
                List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
                List<Product> products = new ArrayList<Product>();
                orderItems.stream().forEach(orderItem -> { products.add(orderItem.getProduct()); });

                HashMap<Long, List<Product>> map = new HashMap<Long, List<Product>>();
                map.put(order.getOrderId(), products);
                //EntityModel<HashMap<Order, List<Product>>> entityModel = EntityModel.of(map);
                order_products.add(map);
            });
            return ResponseEntity.status(HttpStatus.OK).body(order_products);
        }

        String errorMessage = "Customer id is invalid: " + customerId;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createEntity("message", errorMessage));
    }

    public EntityModel<HashMap<String, String>> createEntity(String x, String y) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(x, y);
        EntityModel<HashMap<String, String>> entityModel = EntityModel.of(map);
        return entityModel;
    }
}
