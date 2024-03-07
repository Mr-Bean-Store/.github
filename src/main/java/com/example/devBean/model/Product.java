package com.example.devBean.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import com.example.devBean.model.OrderItem;

@Entity
@Data
@Table(name = "Products")
public class Product {
    
    private @Id @GeneratedValue Long productId;
    private String productName;
    private String description;
    private Double price;
    //private String stock; // I think this means if it's still available...

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> items;

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

    public List<OrderItem> getOrderItems() { return items; }

    public void setOrders(List<OrderItem> items) {
        this.items = items;
    }
}
