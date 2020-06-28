package com.migros.couriertracker.document;

import com.migros.couriertracker.document.key.CourierDateKey;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.GeoIndexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'courierId': 1, 'date': 1}")
@Document(collection = "courierLocationHistory")
public class CourierLocationHistory {
    @Id
    private CourierDateKey courierDateKey;

    @Indexed
    private String courierId;

    @Indexed
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    private Point location;
}
