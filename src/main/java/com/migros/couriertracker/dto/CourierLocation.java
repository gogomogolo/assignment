package com.migros.couriertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourierLocation {
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @NotNull
    @NotBlank
    private String courierId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
