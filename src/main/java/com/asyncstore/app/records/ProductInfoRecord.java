package com.asyncstore.app.records;

public record ProductInfoRecord(
        int productId,
        String productName,
        String productDescription,
        double price,
        double discount,
        String storeName
) {
}
