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
    private String quantity;
    private String price;
    private int productId; // foreign key
    private int orderId; // foreign key

    OrderItem() {}

    OrderItem(String quantity, String price, int productId, int orderId) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getOrderId() {
        return productId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
