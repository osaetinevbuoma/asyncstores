package com.asyncstore.app.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Product extends BaseEntity {
    private String name;
    private String description;

    @OneToMany(targetEntity = Price.class, mappedBy = "product")
    private List<Price> prices;

    public Product() {
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "Product[id=" + this.getId() + ", name=" + this.getName()
                + ", description=" + this.getDescription() + "]";
    }
}
