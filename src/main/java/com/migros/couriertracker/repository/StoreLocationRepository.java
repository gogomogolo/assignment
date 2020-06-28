package com.migros.couriertracker.repository;

import com.migros.couriertracker.document.StoreLocation;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreLocationRepository extends MongoRepository<StoreLocation, String> {
    Optional<List<StoreLocation>> findByLocationNear(Point point, Distance distance);
}
