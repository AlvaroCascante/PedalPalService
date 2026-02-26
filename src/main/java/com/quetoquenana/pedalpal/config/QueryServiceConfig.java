package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.product.application.mapper.ProductMapper;
import com.quetoquenana.pedalpal.product.application.query.ProductQueryService;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServiceConfig {

    @Bean
    public AppointmentQueryService createAppointmentQueryService(
            AppointmentMapper mapper,
            AppointmentRepository repository
    ) {
        return new AppointmentQueryService(
                mapper,
                repository
        );
    }
    @Bean
    public BikeQueryService createBikeQueryService(
            BikeMapper mapper,
            BikeRepository repository
    ) {
        return new BikeQueryService(
                mapper,
                repository
        );
    }

    @Bean
    public BikeHistoryQueryService createBikeHistoryQueryService(
            BikeMapper mapper,
            BikeRepository repository,
            BikeHistoryRepository bikeHistoryRepository
    ) {
        return new BikeHistoryQueryService(
                mapper,
                repository,
                bikeHistoryRepository);
    }

    @Bean
    ProductQueryService createProductQueryService(
            ProductMapper mapper,
            ProductRepository repository,
            ProductPackageRepository productPackageRepository
    ) {
        return new ProductQueryService(
                mapper,
                repository,
                productPackageRepository
        );
    }
    @Bean
    public StoreQueryService createStoreQueryService(
            StoreMapper mapper,
            StoreRepository repository
    ) {
        return new StoreQueryService(
                mapper,
                repository
        );
    }
}