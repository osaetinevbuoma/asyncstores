package com.asyncstore.app.db.repositories;

import com.asyncstore.app.db.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Async
    CompletableFuture<Product> findByProductId(int id);
}
