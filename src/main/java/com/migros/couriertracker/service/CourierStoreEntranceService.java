package com.migros.couriertracker.service;

import com.migros.couriertracker.common.CourierLocationObservable;
import com.migros.couriertracker.common.CourierLocationObserver;
import com.migros.couriertracker.document.CourierEntranceLookup;
import com.migros.couriertracker.dto.CourierLocation;
import com.migros.couriertracker.repository.CourierEntranceLookupRepository;
import com.migros.couriertracker.repository.StoreLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourierStoreEntranceService implements CourierLocationObserver {
    private final StoreLocationRepository storeLocationRepository;
    private final CourierEntranceLookupRepository courierEntranceLookupRepository;

    private final static Distance DISTANCE_IN_METERS = new Distance(1, Metrics.KILOMETERS);
    private final static long EXPIRATION_TIME_IN_MIN = 1L;

    @Autowired
    public CourierStoreEntranceService(
            StoreLocationRepository storeLocationRepository,
            CourierEntranceLookupRepository courierEntranceLookupRepository,
            CourierLocationObservable courierLocationObservable
    ) {
        this.storeLocationRepository = storeLocationRepository;
        this.courierEntranceLookupRepository = courierEntranceLookupRepository;
        courierLocationObservable.addObserver(this);
    }

    public void logEntrance(CourierLocation courierLocation){
        log.info("Entrance status will be calculated for courier with id: <{}>.", courierLocation.getCourierId());
        String courierId = courierLocation.getCourierId();
        double latitude = courierLocation.getLatitude();
        double longitude = courierLocation.getLongitude();

        Point courierPoint = new Point(longitude, latitude);
        var geoResults = storeLocationRepository.findByCoordinateNear(courierPoint, DISTANCE_IN_METERS);
        var storeLocations = geoResults.getContent();

        if (!storeLocations.isEmpty()){
            storeLocations.forEach(storeLocationGeoResult -> {
                var storeLocation = storeLocationGeoResult.getContent();
                String storeName = storeLocation.getName();
                var courierEntranceLookup = courierEntranceLookupRepository
                        .findByCourierIdAndAndStoreName(courierId, storeName);

                if (courierEntranceLookup.isEmpty()){
                    CourierEntranceLookup newCourierEntranceLookup = CourierEntranceLookup
                            .builder()
                            .courierId(courierId)
                            .storeName(storeName)
                            .expiration(EXPIRATION_TIME_IN_MIN)
                            .build();
                    courierEntranceLookupRepository.save(newCourierEntranceLookup);
                    log.info(
                            "Courier with id: <{}> is entering the near of store with name: <{}>",
                            courierId,
                            storeName
                    );
                }
            });
        }
    }

    @Override
    public void handleCourierLocation(CourierLocation courierLocation) {
        logEntrance(courierLocation);
    }
}
