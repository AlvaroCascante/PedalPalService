package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.announcement.application.query.AnnouncementQueryService;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.query.AppointmentQueryService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.query.BikeHistoryQueryService;
import com.quetoquenana.pedalpal.bike.application.query.BikeQueryService;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.port.MediaLookupPort;
import com.quetoquenana.pedalpal.media.application.query.MediaQueryService;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.product.application.mapper.ProductMapper;
import com.quetoquenana.pedalpal.product.application.query.ProductQueryService;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.serviceorder.application.query.ServiceOrderQueryService;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import com.quetoquenana.pedalpal.serviceorder.application.query.GetServiceOrderCommentsQueryService;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.store.application.mapper.StoreMapper;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.domain.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.systemCode.application.mapper.SystemCodeMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryServicesConfig {

    @Bean
    public AnnouncementQueryService createAnnouncementQueryService(
            AnnouncementMapper mapper,
            AnnouncementRepository repository,
            MediaLookupPort mediaLookupPort
    ) {
        return new AnnouncementQueryService(
                mapper,
                repository,
                mediaLookupPort
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
            BikeRepository repository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new BikeQueryService(
                authenticatedUserPort,
                mapper,
                repository
        );
    }

    @Bean
    public BikeHistoryQueryService createBikeHistoryQueryService(
            BikeMapper mapper,
            BikeRepository repository,
            BikeHistoryRepository bikeHistoryRepository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new BikeHistoryQueryService(
                authenticatedUserPort,
                mapper,
                repository,
                bikeHistoryRepository);
    }

    @Bean
    public MediaQueryService createMediaQueryService(
            MediaMapper mapper,
            MediaRepository repository
    ) {
        return new MediaQueryService(mapper, repository);
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
            StoreRepository repository,
            StoreLocationRepository storeLocationRepository
    ) {
        return new StoreQueryService(
                mapper,
                repository,
                storeLocationRepository
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

    @Bean
    public GetServiceOrderCommentsQueryService createGetServiceOrderCommentsQueryService(
            ServiceOrderMapper mapper,
            ServiceOrderCommentRepository commentRepository,
            ServiceOrderRepository repository
    ) {
        return new GetServiceOrderCommentsQueryService(
                mapper,
                commentRepository,
                repository
        );
    }
}