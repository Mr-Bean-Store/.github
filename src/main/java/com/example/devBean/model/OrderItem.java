package com.example.devBean.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    private Long orderItemId;

    @Column
    private String quantity;

    @Column
    private String price;

    @Column
    private int productId; // foreign key

    @Column
    private int orderId; // foreign key

}
