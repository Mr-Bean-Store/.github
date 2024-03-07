package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
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
    @Column
    private int addressId;
    @Column
    private int customerId; // foreign key

    Order() {}

    public Order(String orderDate, String arrivalDate, Double totalPrice, Status status, int addressId, int customerId) {
        this.orderDate = orderDate;
        this.arrivalDate = arrivalDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.addressId = addressId;
        this.customerId = customerId;
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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
