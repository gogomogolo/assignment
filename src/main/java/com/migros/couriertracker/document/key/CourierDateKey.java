package com.migros.couriertracker.document.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourierDateKey implements Serializable {
    private String courierId;
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;
}
