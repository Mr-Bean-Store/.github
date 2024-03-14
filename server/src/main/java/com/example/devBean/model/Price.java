package com.example.devBean.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "prices")
public class Price {
    
    @Id 
    //@GeneratedValue 
    @Column(name = "id")
    private Long priceId;

    @ManyToOne(cascade = CascadeType.ALL) // cascade all will save the data from the address object in the Address table in db
    @JoinColumn(name = "model_id") 
    private ProductModel model; // foreign key

    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_date")
    private String date;

    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "price", cascade = CascadeType.ALL)
    //private List<OrderItem> items;

    public Price() {}

    public Price(Double amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
