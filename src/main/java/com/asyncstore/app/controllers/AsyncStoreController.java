package com.asyncstore.app.controllers;

import com.asyncstore.app.records.ProductInfoRecord;
import com.asyncstore.app.records.ProductRecord;
import com.asyncstore.app.records.StoreRecord;
import com.asyncstore.app.services.AsyncStoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncStoreController {
    private final AsyncStoreService asyncStoreService;

    public AsyncStoreController(AsyncStoreService asyncStoreService) {
        this.asyncStoreService = asyncStoreService;
    }

    @GetMapping("/init")
    public String initDb() {
        this.asyncStoreService.initDbData();
        return "Initialized DB";
    }

    @GetMapping("/product/{id}")
    public CompletableFuture<ProductRecord> getProduct(@PathVariable int id) {
        return this.asyncStoreService.getProduct(id);
    }

    @GetMapping("/products")
    public CompletableFuture<List<ProductInfoRecord>> listProducts(@RequestParam("perPage") Optional<Integer> perPage) {
        int itemsPerPage = perPage.orElse(1);
        return this.asyncStoreService.getProducts(itemsPerPage);
    }

    @GetMapping("/store/{id}")
    public CompletableFuture<StoreRecord> getStore(@PathVariable int id) {
        return this.asyncStoreService.getStore(id);
    }
}
