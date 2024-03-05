package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Order {
    
    @Id @GeneratedValue 
    private Long orderId;

    @Column
    private String orderDate; 

    @Column
    private String shipmentDate;

    @Column
    private String arrivalDate;

    @Column
    private String totalPrice; // I will change this when we integrate the server with the database, since postgresql datatype is money, I will add a Currency dependency
    
    // @Column
    // private Status status; // is status necessary? Should be done in the cli or the backend?
    
    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column
    private int addressId;

    @Column
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

    public String getShipmentDate() {
        return this.shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
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
    
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getLongtude() {
        return longitude;
    }

    public void setLongtude(String longitude) {
        this.longitude = longitude;
    }

    public int getAddressId() {
        return customerId;
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
