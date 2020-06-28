package com.migros.couriertracker.repository;

import com.migros.couriertracker.document.CourierEntranceLookup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourierEntranceLookupRepository extends CrudRepository<CourierEntranceLookup, String> {
    Optional<CourierEntranceLookup> findByCourierIdAndAndStoreName(String courierId, String storeName);
}
