package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServiceConfig {

    @Bean
    public BikeQueryService createBikeQueryService(
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new BikeQueryService(bikeMapper, bikeRepository);
    }

    @Bean
    public BikeHistoryQueryService createBikeHistoryQueryService(
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            BikeHistoryRepository bikeHistoryRepository
    ) {
        return new BikeHistoryQueryService(bikeMapper, bikeRepository, bikeHistoryRepository);
    }
}