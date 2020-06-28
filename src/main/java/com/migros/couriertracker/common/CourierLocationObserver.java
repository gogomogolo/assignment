package com.migros.couriertracker.common;

import com.migros.couriertracker.dto.CourierLocation;

public interface CourierLocationObserver {
    void handleCourierLocation(CourierLocation courierLocation);
}
