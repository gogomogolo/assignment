package com.migros.couriertracker.stream;

import com.migros.couriertracker.common.CourierLocationObservable;
import com.migros.couriertracker.common.CourierLocationObserver;
import com.migros.couriertracker.dto.CourierLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableBinding(Sink.class)
public class CourierLocationListener implements CourierLocationObservable {
    private final static List<CourierLocationObserver> courierLocationObservers = new ArrayList<>();

    @StreamListener(Sink.INPUT)
    public void handle(@NotNull @NotBlank @Valid CourierLocation courierLocation){
        log.info("New courier location is coming.");
        courierLocationObservers.forEach(
                courierLocationObserver -> courierLocationObserver.handleCourierLocation(courierLocation)
        );
    }

    @Override
    public void addObserver(CourierLocationObserver courierLocationObserver) {
        courierLocationObservers.add(courierLocationObserver);
    }
}
