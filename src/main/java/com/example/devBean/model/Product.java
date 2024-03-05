package com.example.devBean.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Product {
    
    private @Id @GeneratedValue Long producId;
    private String productName;
    private String description;
    private String price;
    //private String stock; // I think this means if it's still available...
}
