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
    private String orderDate; 
    //private String shipmentDate; // We changed the shipment date since we are a local online merch store
    private String arrivalDate; // date and time
    private String totalPrice; // I will change this when we integrate the server with the database, since postgresql datatype is money, I will add a Currency dependency
    private Status status; // is status necessary? Should be done in the cli or the backend?
    private int addressId;
    private int customerId; // foreign key

    Order() {}

    Order(String orderDate, String shipmentDate, String totalPrice, String latitude, String longitude, int customerId) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
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
