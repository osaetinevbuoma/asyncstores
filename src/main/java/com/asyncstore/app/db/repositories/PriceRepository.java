package com.asyncstore.app.db.repositories;

import com.asyncstore.app.db.entities.Price;
import com.asyncstore.app.db.entities.Product;
import com.asyncstore.app.db.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
    @Async
    CompletableFuture<Price> findByStoreAndProduct(Store store, Product product);
}
