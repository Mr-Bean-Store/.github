package com.example.devBean.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Orders")
public class Order {
    
    private @Id @GeneratedValue Long orderId;
    @Column
    private String orderDate; 
    //private String shipmentDate; // We changed the shipment date since we are a local online merch store
    @Column
    private String arrivalDate; // date and time
    @Column
    private Double totalPrice; // I will change this when we integrate the server with the database, since postgresql datatype is money, I will add a Currency dependency
    @Column
    private Status status; // is status necessary? Should be done in the cli or the backend?
    
    @ManyToOne(cascade = CascadeType.ALL) // cascade all will save the data from the address object in the Address table in db
    @JoinColumn(name = "fk_customerId") 
    private Customer cust_order; // foreign key

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // private Address addr; // this is unnecessary since the customer object already contains an Address object

    Order() {}

    public Order(String orderDate, String arrivalDate, Double totalPrice, Status status) {
        this.orderDate = orderDate;
        this.arrivalDate = arrivalDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getArrivalDate() {
        return this.arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Customer getCustomer() {
        return cust_order;
    }

    public void setCustomer(Customer cust_order) {
        this.cust_order = cust_order;
    }

    public List<OrderItem> getOrderItems() { return orderItems; }

    public void setOrders(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
