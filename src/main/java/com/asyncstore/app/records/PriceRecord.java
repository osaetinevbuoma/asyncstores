package com.asyncstore.app.records;

public record PriceRecord(
        int id,
        double price,
        double discount,
        ProductRecord productRecord,
        StoreRecord storeRecord
) {
}
