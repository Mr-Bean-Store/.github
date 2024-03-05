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
    
    @Id @GeneratedValue 
    private Long addressId;

    @Column
    private String latitude;

    @Column
    private String longitude;


}
