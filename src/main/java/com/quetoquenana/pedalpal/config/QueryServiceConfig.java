package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.announcement.application.query.AnnouncementQueryService;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.product.mapper.ProductMapper;
import com.quetoquenana.pedalpal.product.application.query.ProductQueryService;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.serviceOrder.application.query.ServiceOrderQueryService;
import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import com.quetoquenana.pedalpal.store.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.systemCode.mapper.SystemCodeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServiceConfig {

    @Bean
    public AnnouncementQueryService createAnnouncementQueryService(
            AnnouncementMapper mapper,
            AnnouncementRepository repository
    ) {
        return new AnnouncementQueryService(
                mapper,
                repository
        );
    }

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
    public ServiceOrderQueryService createServiceOrderQueryService(
            ServiceOrderMapper mapper,
            ServiceOrderRepository repository
    ) {
        return new ServiceOrderQueryService(
                mapper,
                repository
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

    @Bean
    public SystemCodeQueryService createSystemCodeQueryService(
            SystemCodeMapper mapper,
            SystemCodeRepository repository
    ) {
        return new SystemCodeQueryService(
                mapper,
                repository
        );
    }
}