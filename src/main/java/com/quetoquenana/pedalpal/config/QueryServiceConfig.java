package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServiceConfig {

    @Bean
    public BikeQueryService createBikeQueryService(BikeRepository bikeRepository) {
        return new BikeQueryService(bikeRepository);
    }
}