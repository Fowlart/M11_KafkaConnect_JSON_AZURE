package com.fowlart.weatherkafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherKafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherKafkaProducerApplication.class, args);
    }

}
