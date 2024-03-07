package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    private @Id @GeneratedValue Long orderItemId;
    @Column
    private String quantity;
    @Column
    private Double price; // or float, 2 decimal places
    @Column
    private int productId; // foreign key
    @Column
    private int orderId; // foreign key

    public OrderItem() {}

    public OrderItem(String quantity, Double price, int productId, int orderId) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.orderId = orderId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
