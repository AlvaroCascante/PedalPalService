package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServiceConfig {

    @Bean
    public BikeQueryService createBikeQueryService(
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new BikeQueryService(
                bikeMapper,
                bikeRepository
        );
    }

    @Bean
    public BikeHistoryQueryService createBikeHistoryQueryService(
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            BikeHistoryRepository bikeHistoryRepository
    ) {
        return new BikeHistoryQueryService(
                bikeMapper,
                bikeRepository,
                bikeHistoryRepository);
    }

    @Bean
    public StoreQueryService createStoreQueryService(
            StoreMapper storeMapper,
            StoreRepository storeRepository
    ) {
        return new StoreQueryService(
                storeMapper,
                storeRepository
        );
    }
}