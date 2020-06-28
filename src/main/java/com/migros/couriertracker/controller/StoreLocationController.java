package com.migros.couriertracker.controller;

import com.migros.couriertracker.document.StoreLocation;
import com.migros.couriertracker.dto.Store;
import com.migros.couriertracker.repository.StoreLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.Valid;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
@SessionScope
@RestController
@RequestMapping("/location")
public class StoreLocationController {
    private final StoreLocationRepository storeLocationRepository;

    @PostMapping("/stores")
    ResponseEntity<StoreLocation> insertStoreLocation(@RequestBody @Valid Store store){
        StoreLocation storeLocation = StoreLocation
                .builder()
                .name(store.getName())
                .coordinate(new Point(store.getLng(), store.getLat()))
                .build();
        StoreLocation result = storeLocationRepository.save(storeLocation);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
