package com.asyncstore.app.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Store extends BaseEntity {
    private String storeName;
    private String storeAddress;

    @OneToMany(targetEntity = Price.class, mappedBy = "store")
    private List<Price> prices;

    public Store() {
    }

    public Store(String storeName, String storeAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "Store[id=" + this.getId() + ", storeName=" + this.getStoreName() +
                ", storeAddress=" + this.getStoreAddress() + "]";
    }
}
