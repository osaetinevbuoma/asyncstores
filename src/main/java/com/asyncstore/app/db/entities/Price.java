package com.asyncstore.app.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Price extends BaseEntity {
    private Double price;
    private Double discount;

    @ManyToOne(targetEntity = Product.class)
    private Product product;

    @ManyToOne(targetEntity = Store.class)
    private Store store;

    public Price() {
    }

    public Price(Store store, Product product, Double price, Double discount) {
        this.store = store;
        this.product = product;
        this.price = price;
        this.discount = discount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Price[id=" + this.getId() + ", store=" + this.getStore() +
                ", product=" + this.getProduct() + ", price=" + this.getPrice() +
                ", discount=" + this.getDiscount() + "]";
    }
}
