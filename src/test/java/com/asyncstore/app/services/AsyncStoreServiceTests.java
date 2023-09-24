package com.asyncstore.app.services;

import com.asyncstore.app.records.ProductRecord;
import com.asyncstore.app.records.StoreRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
class AsyncStoreServiceTests {
    @Autowired
    AsyncStoreService asyncStoreService;

    @BeforeEach
    void setup() {
        asyncStoreService.initDbData();
    }

    @Test
    void testGetProduct() {
        CompletableFuture<ProductRecord> product = asyncStoreService.getProduct(1);
        product.thenAccept(p -> {
            Assertions.assertEquals(1, p.id());
            Assertions.assertEquals("", p.name());
            Assertions.assertEquals("", p.description());
        });
    }

    @Test
    void testGetStore() {
        CompletableFuture<StoreRecord> store = asyncStoreService.getStore(1);
        store.thenAccept(s -> {
            Assertions.assertEquals(1, s.id());
            Assertions.assertEquals("", s.storeName());
            Assertions.assertEquals("", s.storeAddress());
        });
    }
}
