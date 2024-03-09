package com.example.devBean.dataloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.example.devBean.model.Customer;
import com.example.devBean.model.Order;
import com.example.devBean.model.OrderItem;
import com.example.devBean.model.Product;
import com.example.devBean.repository.AddressRepository;
import com.example.devBean.repository.CustomerRepository;
import com.example.devBean.repository.OrderItemRepository;
import com.example.devBean.repository.OrderRepository;
import com.example.devBean.repository.ProductRepository;
import com.example.devBean.model.Address;
import com.example.devBean.model.Status; // port -> 5432

@Configuration
@Slf4j
public class LoadDatabase {
    
    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepo, OrderRepository orderRepo, AddressRepository addressRepo, OrderItemRepository orderItemRepo, ProductRepository productRepo) {
        return args -> {
            log.info("Preloading " + customerRepo.save(new Customer("Bilbo", "Baggins", "biblo.baggins@gmail.com")));
            log.info("Preloading " + customerRepo.save(new Customer("Mithrindir", "Gandalf","mithrindir.gandalf@gmail.com")));
            
            log.info("Preloading " + addressRepo.save(new Address(" -26.144836", "28.042178")));
            
            //log.info("Preloading " + orderRepo.save(new Order("01-03-2024", "05-03-2024")));
            //log.info("Preloading " + orderRepo.save(new Order("28-02-2024", "15-03-2024")));

            //log.info("Preloading " + orderItemRepo.save(new OrderItem("1", (double)1000, 1, 1)));
            //log.info("Preloading " + orderItemRepo.save(new OrderItem("2", (double)1500, 2, 2)));

            //log.info("Preloading " + productRepo.save(new Product("Mr Beans Yellow Car", "State of the art classic car", (double)1000)));
            //log.info("Preloading " + productRepo.save(new Product("Mr Beans Suit", "Classic Mr Bean suit, made during Queen Victoria's era", (double)1500)));

        };
    }
}
