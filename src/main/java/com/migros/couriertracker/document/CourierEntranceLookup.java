package com.migros.couriertracker.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "courierEntrance",timeToLive = 60L)
public class CourierEntranceLookup implements Serializable {
    @Id
    private String courierIdStoreName;
}
