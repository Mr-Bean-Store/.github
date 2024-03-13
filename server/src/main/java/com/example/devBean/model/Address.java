package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author: Mr Bean 
 * @apiNote: My assumption is that this class is for the destination address of the order 
 * @version: 1.0
 */

@Entity
@Data
public class Address {
     
    private @Id @GeneratedValue Long addressId;
    @Column
    private String latitude;
    @Column
    private String longitude;

    Address() {}

    public Address(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

}
