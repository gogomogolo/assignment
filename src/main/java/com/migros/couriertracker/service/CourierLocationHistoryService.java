package com.migros.couriertracker.service;

import com.migros.couriertracker.common.CourierLocationObservable;
import com.migros.couriertracker.common.CourierLocationObserver;
import com.migros.couriertracker.document.CourierLocationHistory;
import com.migros.couriertracker.document.key.CourierDateKey;
import com.migros.couriertracker.dto.CourierLocation;
import com.migros.couriertracker.dto.CourierTotalDistance;
import com.migros.couriertracker.exception.CourierIdNotFoundException;
import com.migros.couriertracker.repository.CourierLocationHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourierLocationHistoryService implements CourierLocationObserver {
    private final CourierLocationHistoryRepository courierLocationHistoryRepository;

    @Autowired
    public CourierLocationHistoryService(
            CourierLocationHistoryRepository courierLocationHistoryRepository,
            CourierLocationObservable courierLocationObservable
    ) {
        this.courierLocationHistoryRepository = courierLocationHistoryRepository;
        courierLocationObservable.addObserver(this);
    }

    public CourierTotalDistance getTotalDistanceById(String courierId) {
        log.info("Total distance of courier with id: <{}> is getting.", courierId);
        var courierLocationHistories = courierLocationHistoryRepository
                .findByCourierIdOrderByDateAsc(courierId);
        double distanceSum = 0.0;

        if (courierLocationHistories.get().size() != 0){
            var courierLocations = courierLocationHistories.get();

            for (int i=1; i<courierLocations.size(); i++){
                distanceSum += distanceBetween(
                        courierLocations.get(i-1).getLocation(),
                        courierLocations.get(i).getLocation()
                );
            }
        } else {
            throw new CourierIdNotFoundException(
                    String.format("Total distance for courier with id: <%s> is not found", courierId)
            );
        }
        log.info("Total distance of courier with id: <{}> was calculated as <{}> kilometers.", courierId, distanceSum);

        return CourierTotalDistance
                .builder()
                .courierId(courierId)
                .totalDistance(distanceSum)
                .build();
    }

    private double distanceBetween(Point p1, Point p2) {
        double lat1 = p1.getX(), lon1 = p1.getY();
        double lat2 = p2.getX(), lon2 = p2.getY();

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }

    @Override
    public void handleCourierLocation(CourierLocation courierLocation) {
        CourierLocationHistory courierLocationHistory = CourierLocationHistory
                .builder()
                .courierDateKey(CourierDateKey.builder().courierId(courierLocation.getCourierId()).date(courierLocation.getDate()).build())
                .courierId(courierLocation.getCourierId())
                .date(courierLocation.getDate())
                .location(new Point(courierLocation.getLatitude(), courierLocation.getLongitude()))
                .build();
        courierLocationHistoryRepository.save(courierLocationHistory);
    }
}
