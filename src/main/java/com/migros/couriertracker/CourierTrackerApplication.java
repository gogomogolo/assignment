package com.migros.couriertracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@EnableMongoRepositories
@SpringBootApplication
public class CourierTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierTrackerApplication.class, args);
    }

}
