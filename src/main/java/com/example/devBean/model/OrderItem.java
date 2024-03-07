package com.example.devBean.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.example.devBean.model.Product;

@Entity
@Data
@Table(name = "OrderItems")
public class OrderItem {

    private @Id @GeneratedValue Long orderItemId;
    @Column
    private String quantity;
    @Column
    private Double price; // or float, 2 decimal places
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_productId")
    private Product product;
    
    @ManyToOne(cascade = CascadeType.ALL) // cascade all will save the data from the address object in the Address table in db
    @JoinColumn(name = "fk_orderId") 
    private Order order; // foreign key

    public OrderItem() {}

    public OrderItem(String quantity, Double price) {
        this.quantity = quantity;
        this.price = price;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
