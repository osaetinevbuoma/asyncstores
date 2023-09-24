package com.asyncstore.app.dtomappers;

import com.asyncstore.app.db.entities.Store;
import com.asyncstore.app.records.StoreRecord;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StoreRecordMapper implements Function<Store, StoreRecord> {
    @Override
    public StoreRecord apply(Store store) {
        return new StoreRecord(store.getStoreId(), store.getStoreName(), store.getStoreAddress());
    }
}
