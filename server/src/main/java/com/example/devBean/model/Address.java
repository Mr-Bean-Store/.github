package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author: Mr Bean 
 * @apiNote: My assumption is that this class is for the destination address of the order 
 * @version: 1.0
 */

@Entity
@Data
@Table(name = "addresses")
public class Address {
     
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long addressId;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    Address() {}

    public Address(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
