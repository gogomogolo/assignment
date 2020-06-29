package com.migros.couriertracker.service;

import com.migros.couriertracker.common.CourierLocationObservable;
import com.migros.couriertracker.document.CourierLocationHistory;
import com.migros.couriertracker.document.key.CourierDateKey;
import com.migros.couriertracker.dto.CourierLocation;
import com.migros.couriertracker.dto.CourierTotalDistance;
import com.migros.couriertracker.exception.CourierIdNotFoundException;
import com.migros.couriertracker.repository.CourierLocationHistoryRepository;
import com.migros.couriertracker.util.TestUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.geo.Point;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import static org.testng.Assert.*;

public class CourierLocationHistoryServiceTest {
    @Mock
    private CourierLocationHistoryRepository courierLocationHistoryRepository;

    @Mock
    private CourierLocationObservable courierLocationObservable;

    @InjectMocks
    private CourierLocationHistoryService underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CourierLocationHistoryService(
                courierLocationHistoryRepository,courierLocationObservable
        );
    }

    @Test(expectedExceptions = CourierIdNotFoundException.class)
    public void shouldThrowCourierIdNotFoundException() {
        // GIVEN
        String courierId = "unfoundId";
        Optional<List<CourierLocationHistory>> emptyCourierHistories =
                Optional.of(new ArrayList<>());
        when(courierLocationHistoryRepository.findByCourierIdOrderByDateAsc(courierId))
                .thenReturn(emptyCourierHistories);

        // WHEN
        CourierTotalDistance courierTotalDistance = underTest.getTotalDistanceById(courierId);

        // THEN
        verify(courierLocationHistoryRepository, times(1))
                .findByCourierIdOrderByDateAsc(courierId);
        assertNull(courierTotalDistance);
    }

    @Test
    public void shouldReturnDistanceValueAsZero() {
        // GIVEN
        String courierId = "Cid_1";
        CourierLocation courierLocation = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-10-31T01:30Z"))
                .latitude(40.0)
                .longitude(40.0)
                .build();
        CourierLocationHistory courierLocationHistory = CourierLocationHistory
                .builder()
                .courierDateKey(CourierDateKey.builder().courierId(courierLocation.getCourierId()).date(courierLocation.getDate()).build())
                .courierId(courierLocation.getCourierId())
                .date(courierLocation.getDate())
                .location(new Point(courierLocation.getLongitude(), courierLocation.getLatitude()))
                .build();
        Optional<List<CourierLocationHistory>> oneCourierHistories =
                Optional.of(new ArrayList<>(){{add(courierLocationHistory);}});
        when(courierLocationHistoryRepository.findByCourierIdOrderByDateAsc(courierId))
                .thenReturn(oneCourierHistories);

        // WHEN
        CourierTotalDistance courierTotalDistance = underTest.getTotalDistanceById(courierId);

        // THEN
        verify(courierLocationHistoryRepository, times(1))
                .findByCourierIdOrderByDateAsc(courierId);
        assertNotNull(courierTotalDistance);
        assertEquals(courierTotalDistance.getTotalDistance(), 0.0);
    }

    @Test
    public void shouldReturnDistanceValue() {
        // GIVEN
        String courierId = "Cid_1";
        CourierLocation courierLocation = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-10-31T01:30Z"))
                .latitude(40.0)
                .longitude(40.0)
                .build();
        CourierLocationHistory courierLocationHistory = CourierLocationHistory
                .builder()
                .courierDateKey(CourierDateKey.builder().courierId(courierLocation.getCourierId()).date(courierLocation.getDate()).build())
                .courierId(courierLocation.getCourierId())
                .date(courierLocation.getDate())
                .location(new Point(courierLocation.getLongitude(), courierLocation.getLatitude()))
                .build();

        CourierLocation courierLocation2 = CourierLocation
                .builder()
                .courierId(courierId)
                .date(TestUtil.createDate("2000-11-31T01:30Z"))
                .latitude(50.0)
                .longitude(50.0)
                .build();
        CourierLocationHistory courierLocationHistory2 = CourierLocationHistory
                .builder()
                .courierDateKey(CourierDateKey.builder().courierId(courierLocation2.getCourierId()).date(courierLocation2.getDate()).build())
                .courierId(courierLocation2.getCourierId())
                .date(courierLocation2.getDate())
                .location(new Point(courierLocation2.getLongitude(), courierLocation2.getLatitude()))
                .build();

        Optional<List<CourierLocationHistory>> courierHistories =
                Optional.of(new ArrayList<>(){{add(courierLocationHistory);add(courierLocationHistory2);}});
        when(courierLocationHistoryRepository.findByCourierIdOrderByDateAsc(courierId))
                .thenReturn(courierHistories);

        // WHEN
        CourierTotalDistance courierTotalDistance = underTest.getTotalDistanceById(courierId);

        // THEN
        verify(courierLocationHistoryRepository, times(1))
                .findByCourierIdOrderByDateAsc(courierId);
        assertNotNull(courierTotalDistance);
        assertEquals(courierTotalDistance.getTotalDistance()
                , TestUtil.distanceBetween(
                        courierLocationHistory.getLocation(),
                        courierLocationHistory2.getLocation()));
    }
}