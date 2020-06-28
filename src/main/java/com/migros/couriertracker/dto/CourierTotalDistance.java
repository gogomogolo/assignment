package com.migros.couriertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CourierTotalDistance {
    private final String courierId;
    private final double totalDistance;
}
