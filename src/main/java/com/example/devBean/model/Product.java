package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Product {
    
    private @Id @GeneratedValue Long productId;
    @Column
    private String productName;
    @Column
    private String description;
    @Column
    private Double price;
    //private String stock; // I think this means if it's still available...

    public Product() {}

    public Product(String productName, String description, Double price) {
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
