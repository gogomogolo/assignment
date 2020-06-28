package com.migros.couriertracker.repository;

import com.migros.couriertracker.document.CourierLocationHistory;
import com.migros.couriertracker.document.key.CourierDateKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierLocationHistoryRepository extends MongoRepository<CourierLocationHistory, CourierDateKey> {
    Optional<List<CourierLocationHistory>> findByCourierIdOrderByDateAsc(String courierId);
}
