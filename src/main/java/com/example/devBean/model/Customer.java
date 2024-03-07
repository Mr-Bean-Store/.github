package com.example.devBean.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity // this annotation specifies that this Java class is mapped to the database table.
@Data
@Table(name = "Customers")
public class Customer {
    
    private @Id @GeneratedValue Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    // creates fk cust_addressId
    @OneToOne(cascade = CascadeType.ALL) // cascade all will save the data from the address object in the Address table in db
    @JoinColumn(name = "fk_addressId") 
    private Address cust_addr; // a separate table will be made for address, therefore a separate model class
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cust_order", cascade = CascadeType.ALL)
    private List<Order> orders;

    Customer() {}

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return cust_addr; // for now
    }

    public void setAddress(Address cust_addr) {
        this.cust_addr = cust_addr;
    }

    public List<Order> getOrders() { return orders; }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
