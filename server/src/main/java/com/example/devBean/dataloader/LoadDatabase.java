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

@Configuration
@Slf4j
public class LoadDatabase {
    
    @Bean
    CommandLineRunner initDatabase(OrderItemRepository orderItemRepo, ProductRepository productRepo) {
        return args -> {
         
        
            //log.info("Preloading " + orderItemRepo.save(new OrderItem("1", (double)1000, 1, 1)));
            //log.info("Preloading " + orderItemRepo.save(new OrderItem("2", (double)1500, 2, 2)));

            //log.info("Preloading " + productRepo.save(new Product("Mr Beans Yellow Car", "State of the art classic car", (double)1000)));
            //log.info("Preloading " + productRepo.save(new Product("Mr Beans Suit", "Classic Mr Bean suit, made during Queen Victoria's era", (double)1500)));
            /*
             * 
             * 
             */

        };
    }
}