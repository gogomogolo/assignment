package com.migros.couriertracker.service;

import com.migros.couriertracker.common.CourierLocationObservable;
import com.migros.couriertracker.document.CourierEntranceLookup;
import com.migros.couriertracker.document.StoreLocation;
import com.migros.couriertracker.dto.CourierLocation;
import com.migros.couriertracker.repository.CourierEntranceLookupRepository;
import com.migros.couriertracker.repository.StoreLocationRepository;
import com.migros.couriertracker.util.TestUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.geo.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class CourierStoreEntranceServiceTest {
    @Mock
    private StoreLocationRepository storeLocationRepository;

    @Mock
    private CourierEntranceLookupRepository courierEntranceLookupRepository;

    @Mock
    private CourierLocationObservable courierLocationObservable;

    @InjectMocks
    private CourierStoreEntranceService underTest;


    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CourierStoreEntranceService(
                storeLocationRepository,
                courierEntranceLookupRepository,
                courierLocationObservable
        );
    }


    @Test
    public void shouldNotLogEntrance() {
        // GIVEN
        String courierId = "Cid_1";
        CourierLocation courierLocation = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-10-31T01:30Z"))
                .latitude(40.0)
                .longitude(40.0)
                .build();
        GeoResults<StoreLocation> noLocation =
                new GeoResults<StoreLocation>(new ArrayList<>());
        when(storeLocationRepository.findByCoordinateNear(any(), any()))
                .thenReturn(noLocation);

        // WHEN
        underTest.logEntrance(courierLocation);

        // THEN
        verify(storeLocationRepository, times(1))
                .findByCoordinateNear(any(), any());
        verify(courierEntranceLookupRepository, times(0))
                .findById(any());
        verify(courierEntranceLookupRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldLogEntrance() {
        // GIVEN
        String courierId = "Cid_1";
        CourierLocation courierLocation = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-10-31T01:30Z"))
                .latitude(40.0)
                .longitude(40.0)
                .build();
        StoreLocation storeLocation = StoreLocation
                .builder()
                .coordinate(new Point(40.0, 40.0))
                .name("Migros MMM Malatya")
                .build();
        GeoResult<StoreLocation> geoResult = new GeoResult(storeLocation, new Distance(0.1, Metrics.KILOMETERS));
        GeoResults<StoreLocation> location =
                new GeoResults<>(new ArrayList<>(){{add(geoResult);}});
        when(storeLocationRepository.findByCoordinateNear(any(), any()))
                .thenReturn(location);
        when(courierEntranceLookupRepository.findById(any()))
                .thenReturn(Optional.empty());

        // WHEN
        underTest.logEntrance(courierLocation);

        // THEN
        verify(storeLocationRepository, times(1))
                .findByCoordinateNear(any(), any());
        verify(courierEntranceLookupRepository, times(1))
                .findById(any());
        verify(courierEntranceLookupRepository, times(1))
                .save(any());
    }

    @Test
    public void shouldNotLogEntranceAfterCountAsEntrance() {
        // GIVEN
        String courierId = "Cid_1";
        CourierLocation courierLocation = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-10-31T01:30Z"))
                .latitude(40.0)
                .longitude(40.0)
                .build();
        StoreLocation storeLocation = StoreLocation
                .builder()
                .coordinate(new Point(40.0, 40.0))
                .name("Migros MMM Malatya")
                .build();
        GeoResult<StoreLocation> geoResult = new GeoResult(storeLocation, new Distance(0.1, Metrics.KILOMETERS));
        GeoResults<StoreLocation> location =
                new GeoResults<>(new ArrayList<>(){{add(geoResult);}});
        when(storeLocationRepository.findByCoordinateNear(any(), any()))
                .thenReturn(location);
        when(courierEntranceLookupRepository.findById(any()))
                .thenReturn(Optional.of(new CourierEntranceLookup()));

        // WHEN
        underTest.logEntrance(courierLocation);

        // THEN
        verify(storeLocationRepository, times(1))
                .findByCoordinateNear(any(), any());
        verify(courierEntranceLookupRepository, times(1))
                .findById(any());
        verify(courierEntranceLookupRepository, times(0))
                .save(any());
    }
}