package com.asyncstore.app.services;

import com.asyncstore.app.db.entities.Price;
import com.asyncstore.app.db.entities.Product;
import com.asyncstore.app.db.entities.Store;
import com.asyncstore.app.db.repositories.PriceRepository;
import com.asyncstore.app.db.repositories.ProductRepository;
import com.asyncstore.app.db.repositories.StoreRepository;
import com.asyncstore.app.dtomappers.ProductRecordMapper;
import com.asyncstore.app.dtomappers.StoreRecordMapper;
import com.asyncstore.app.records.ProductInfoRecord;
import com.asyncstore.app.records.ProductRecord;
import com.asyncstore.app.records.StoreRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class AsyncStoreService {
    private static final Logger log = LoggerFactory.getLogger(AsyncStoreService.class.getName());
    private final PriceRepository priceRepository;
    private final ProductRecordMapper productRecordMapper;
    private final ProductRepository productRepository;
    private final StoreRecordMapper storeRecordMapper;
    private final StoreRepository storeRepository;
    private final Executor asyncExecutor;

    public AsyncStoreService(PriceRepository priceRepository, ProductRecordMapper productRecordMapper,
                             ProductRepository productRepository, StoreRecordMapper storeRecordMapper,
                             StoreRepository storeRepository, Executor asyncExecutor) {
        this.priceRepository = priceRepository;
        this.productRecordMapper = productRecordMapper;
        this.productRepository = productRepository;
        this.storeRecordMapper = storeRecordMapper;
        this.storeRepository = storeRepository;
        this.asyncExecutor = asyncExecutor;
    }

    @Async
    public void initDbData() {
        CompletableFuture<List<Product>> products = saveProductData();
        CompletableFuture<List<Store>> stores = saveStoreData();
        CompletableFuture.allOf(products, stores)
                .thenAcceptAsync(v -> {
                    CompletableFuture<List<Price>> prices = savePriceData(products.join(), stores.join());
                    prices.join();
                });
    }

    private CompletableFuture<List<Product>> saveProductData() {
        String fileName = "data/product_data.csv";
        return CompletableFuture.supplyAsync(() -> parseProductCsv(fileName), this.asyncExecutor);
    }

    private CompletableFuture<List<Store>> saveStoreData() {
        String fileName = "data/store_data.csv";
        return CompletableFuture.supplyAsync(() -> parseStoreCsv(fileName), this.asyncExecutor);
    }

    private CompletableFuture<List<Price>> savePriceData(List<Product> products, List<Store> stores) {
        String fileName = "data/price_data.csv";
        return CompletableFuture.supplyAsync(() -> parsePriceCsv(fileName, products, stores),
                this.asyncExecutor);
    }

    private List<Product> parseProductCsv(String fileName) {
        List<Product> products = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        log.info("Parsing Product CSV");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    resource.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], data[1]);
                products.add(product);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        products = this.productRepository.saveAll(products);
        log.info("Finished parsing Product CSV");
        return products;
    }

    private List<Store> parseStoreCsv(String fileName) {
        List<Store> stores = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        log.info("Parsing Store CSV");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    resource.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                Store store = new Store(data[0], data[1]);
                stores.add(store);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        stores = this.storeRepository.saveAll(stores);
        log.info("Finished parsing Store CSV");
        return stores;
    }

    private List<Price> parsePriceCsv(String fileName, List<Product> products, List<Store> stores) {
        List<Price> prices = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        log.info("Parsing Price CSV");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    resource.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                Price price = new Price();
                price.setPrice(Double.parseDouble(data[0]));
                price.setDiscount(Double.parseDouble(data[1]));
                prices.add(price);
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        for (Price price : prices) {
            for (Product product : products) {
                price.setProduct(product);
            }

            for (Store store : stores) {
                price.setStore(store);
            }
        }

        prices = this.priceRepository.saveAll(prices);
        log.info("Finished parsing Price CSV");
        return prices;
    }

    @Async
    public CompletableFuture<ProductRecord> getProduct(int id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Product> productCompletableFuture = CompletableFuture
                .completedFuture(product.get());
        return productCompletableFuture.thenApply(productRecordMapper);
    }

    @Async
    public CompletableFuture<List<ProductInfoRecord>> getProducts(int perPage) {
        CompletableFuture<List<Product>> products = CompletableFuture.supplyAsync(() ->
                        this.productRepository.findAll(PageRequest.ofSize(perPage)).stream().toList(),
                this.asyncExecutor);
        CompletableFuture<List<Store>> stores = CompletableFuture.supplyAsync(() ->
                        this.storeRepository.findAll(PageRequest.ofSize(perPage)).stream().toList(),
                this.asyncExecutor);

        List<Product> productList = products.join();
        List<Store> storeList = stores.join();
        List<ProductInfoRecord> productInfoRecords = new ArrayList<>();
        for (int i = 0; i < perPage; i++) {
            Store store = storeList.get(i);
            Product product = productList.get(i);
            CompletableFuture<Price> price = this.priceRepository.findByStoreAndProduct(store, product);
            price.thenAcceptAsync(v -> productInfoRecords.add(
                    new ProductInfoRecord(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            price.join().getPrice(),
                            price.join().getDiscount(),
                            store.getStoreName()
                    )
            ));
        }

        return CompletableFuture.completedFuture(productInfoRecords);
    }

    @Async
    public CompletableFuture<StoreRecord> getStore(int id) {
        Optional<Store> store = this.storeRepository.findById(id);
        if (store.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        CompletableFuture<Store> storeCompletableFuture = CompletableFuture
                .completedFuture(store.get());
        return storeCompletableFuture.thenApply(storeRecordMapper);
    }
}
