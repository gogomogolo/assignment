package com.migros.couriertracker.repository;

import com.migros.couriertracker.document.StoreLocation;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreLocationRepository extends MongoRepository<StoreLocation, String> {
    GeoResults<StoreLocation> findByCoordinateNear(Point point, Distance distance);
}
