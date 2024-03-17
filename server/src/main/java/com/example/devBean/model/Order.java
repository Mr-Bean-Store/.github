package com.example.devBean.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "orders")
public class Order {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") 
    private Long orderId;
    
    @ManyToOne(cascade = CascadeType.ALL) // cascade all will save the data from the customer object in the Customer table in db
    @JoinColumn(name = "customer_id") 
    private Customer customer; // foreign key

    @ManyToOne(cascade = CascadeType.ALL) // cascade all will save the data from the address object in the Address table in db
    @JoinColumn(name = "delivery_addess_id") 
    private Address delivery; // a separate table will be made for address, therefore a separate model class

    @Column(name = "created_date")
    private Timestamp orderDate; 

    @Column(name = "completed_date")
    private Timestamp arrivalDate; // date and time

    Order() {}

    public Order(Timestamp orderDate, Timestamp arrivalDate) {
        this.orderDate = orderDate;
        this.arrivalDate = arrivalDate;
    }
}
