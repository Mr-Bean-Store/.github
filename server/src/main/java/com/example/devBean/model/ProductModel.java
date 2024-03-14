package com.example.devBean.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "models")
public class ProductModel {
    
    @Id 
    @GeneratedValue 
    @Column(name = "id")
    private Long model_id;

    @Column(name = "description") 
    private String description;

    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "model", cascade = CascadeType.ALL)
    //private List<Price> prices;

    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "model", cascade = CascadeType.ALL)
    //private List<Product> products;

    public ProductModel() {}

    public ProductModel(String description) {
        this.description = description;
    }
}
