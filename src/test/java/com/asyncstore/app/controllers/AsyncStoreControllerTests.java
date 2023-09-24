package com.asyncstore.app.controllers;

import com.asyncstore.app.records.ProductRecord;
import com.asyncstore.app.records.StoreRecord;
import com.asyncstore.app.services.AsyncStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Disabled
@WebMvcTest(controllers = AsyncStoreController.class)
class AsyncStoreControllerTests {
    List<ProductRecord> products = new ArrayList<>();
    List<StoreRecord> stores = new ArrayList<>();

    @MockBean
    AsyncStoreService asyncStoreService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        products = List.of(new ProductRecord(1, "Cheese", "Fantastic cheese"));
        stores = List.of(new StoreRecord(1, "Google", "Mountain View, CA"));
    }

    @Test
    void testGetProduct() throws Exception {
        Mockito.when(asyncStoreService.getProduct(1)).thenReturn(CompletableFuture.completedFuture(products.get(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/product/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{}"));
    }
}
