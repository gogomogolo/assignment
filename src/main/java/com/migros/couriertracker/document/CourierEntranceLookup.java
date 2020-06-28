package com.migros.couriertracker.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("courierEntranceLookup")
public class CourierEntranceLookup {
    @Id
    private String courierId;

    @Indexed
    private String storeName;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long expiration;
}
