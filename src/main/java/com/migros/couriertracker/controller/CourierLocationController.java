package com.migros.couriertracker.controller;

import com.migros.couriertracker.dto.CourierTotalDistance;
import com.migros.couriertracker.service.CourierLocationHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@CrossOrigin
@SessionScope
@RestController
@RequestMapping("/distance")
public class CourierLocationController {
    private final CourierLocationHistoryService courierLocationHistoryService;

    @GetMapping("/couriers/{id}")
    ResponseEntity<CourierTotalDistance> getTotalDistance(
            @PathVariable(value = "id") @NotBlank String id
    ){
        CourierTotalDistance courierTotalDistance = courierLocationHistoryService.getTotalDistanceById(id);
        return ResponseEntity
                .ok()
                .body(courierTotalDistance);
    }
}
