package com.migros.couriertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Store {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
