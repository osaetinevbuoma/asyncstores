package com.asyncstore.app.dtomappers;

import com.asyncstore.app.db.entities.Product;
import com.asyncstore.app.records.ProductRecord;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductRecordMapper implements Function<Product, ProductRecord> {
    @Override
    public ProductRecord apply(Product product) {
        return new ProductRecord(product.getProductId(), product.getName(), product.getDescription());
    }
}
