package com.example.devBean.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Product {
    
    private @Id @GeneratedValue Long productId;
    private String productName;
    private String description;
    private String price;
    //private String stock; // I think this means if it's still available...

    Product() {}

    Product(String productName, String description, String price) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
