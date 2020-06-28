package com.migros.couriertracker.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "storeLocation")
public class StoreLocation {
    @Id
    private String name;

    @GeoSpatialIndexed
    private Point location;
}
