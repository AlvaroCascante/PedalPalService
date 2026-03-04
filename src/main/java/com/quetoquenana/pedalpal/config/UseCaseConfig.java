package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementStatusUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.appointment.application.usecase.*;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.serviceOrder.application.port.ServiceOrderPort;
import com.quetoquenana.pedalpal.store.domain.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.media.application.port.CdnUrlProvider;
import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.UploadMediaUseCase;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.media.mapper.MediaMapper;
import com.quetoquenana.pedalpal.security.application.OwnershipValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    /*
     * Announcements Use Cases
     */
    @Bean
    public CreateAnnouncementUseCase createCreateAnnouncementUseCase(
            AnnouncementMapper mapper,
            AnnouncementRepository repository,
            UploadMediaPort uploadMediaPort
    ) {
        return new CreateAnnouncementUseCase(
                mapper,
                repository,
                uploadMediaPort
        );
    }

    @Bean
    public UpdateAnnouncementStatusUseCase createUpdateAnnouncementStatusUseCase(
            AnnouncementRepository repository,
            AnnouncementMapper mapper
    ) {
        return new UpdateAnnouncementStatusUseCase(
                repository,
                mapper
        );
    }

    @Bean
    public UpdateAnnouncementUseCase createUpdateAnnouncementUseCase(
            AnnouncementRepository repository,
            AnnouncementMapper mapper
    ) {
        return new UpdateAnnouncementUseCase(
                repository,
                mapper
        );
    }

    /*
     * Appointment Use Cases
     */
    @Bean
    public CancelAppointmentUseCase createCancelAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository repository,
            ServiceOrderPort serviceOrderPort
    ) {
        return new CancelAppointmentUseCase(
                mapper,
                repository,
                serviceOrderPort
        );
    }

    @Bean
    public ConfirmAppointmentUseCase createConfirmAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository repository,
            StoreLocationRepository storeLocationRepository,
            ServiceOrderPort serviceOrderPort
    ) {
        return new ConfirmAppointmentUseCase(
                mapper,
                repository,
                storeLocationRepository,
                serviceOrderPort
        );
    }

    @Bean
    public CreateAppointmentUseCase createCreateAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository appointmentRepository,
            BikeRepository bikeRepository,
            ProductRepository productRepository,
            ProductPackageRepository productPackageRepository
    ) {
        return new CreateAppointmentUseCase(
                mapper,
                appointmentRepository,
                bikeRepository,
                productRepository,
                productPackageRepository
        );
    }

    @Bean
    public UpdateAppointmentStatusUseCase createUpdateAppointmentStatusUseCase(
            AppointmentMapper mapper,
            AppointmentRepository appointmentRepository
    ) {
        return new UpdateAppointmentStatusUseCase(
                mapper,
                appointmentRepository
        );
    }

    @Bean
    public UpdateAppointmentUseCase createUpdateAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository appointmentRepository
    ) {
        return new UpdateAppointmentUseCase(
                mapper,
                appointmentRepository
        );
    }

    /*
     * Bike Use Cases
     */
    @Bean
    public AddBikeComponentUseCase createAddBikeComponentUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new AddBikeComponentUseCase(
                bikeMapper,
                bikeRepository,
                systemCodeRepository,
                eventPublisher
        );
    }

    @Bean
    public CreateBikeUseCase createCreateBikeUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new CreateBikeUseCase(
                bikeMapper,
                bikeRepository,
                eventPublisher
        );
    }

    @Bean
    public ReplaceBikeComponentUseCase createReplaceBikeComponentUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new ReplaceBikeComponentUseCase(
                bikeMapper,
                bikeRepository,
                systemCodeRepository,
                eventPublisher
        );
    }

    @Bean
    public UpdateBikeStatusUseCase createUpdateBikeStatusUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new UpdateBikeStatusUseCase(
                bikeMapper,
                bikeRepository,
                eventPublisher
        );
    }

    @Bean
    public UpdateBikeUseCase createUpdateBikeUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new UpdateBikeUseCase(
                bikeMapper,
                bikeRepository,
                eventPublisher
        );
    }

    @Bean
    public UpdateBikeComponentUseCase createUpdateBikeComponentUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new UpdateBikeComponentUseCase(
                bikeMapper,
                bikeRepository,
                systemCodeRepository,
                eventPublisher);
    }

    @Bean
    public UpdateBikeComponentStatusUseCase createUpdateBikeComponentStatusUseCase(
            ApplicationEventPublisher eventPublisher,
            BikeMapper bikeMapper,
            BikeRepository bikeRepository
    ) {
        return new UpdateBikeComponentStatusUseCase(
                bikeMapper,
                bikeRepository,
                eventPublisher
        );
    }

    @Bean
    public CreateBikeUploadMediaUseCase createCreateBikeUploadMediaUseCase(
            BikeMapper bikeMapper,
            BikeRepository bikeRepository,
            UploadMediaPort uploadMediaPort
    ) {
        return new CreateBikeUploadMediaUseCase(
                bikeMapper,
                bikeRepository,
                uploadMediaPort
        );
    }

    /*
     * Media Use Cases
     */
    @Bean
    public ConfirmUploadUseCase createConfirmUploadUseCase(
            MediaRepository repository,
            MediaMapper mapper,
            CdnUrlProvider cdnUrlProvider
    ) {
        return new ConfirmUploadUseCase(
                repository,
                mapper,
                cdnUrlProvider
        );
    }

    @Bean
    public UploadMediaUseCase createGenerateUploadUrlUseCase(
            MediaRepository repository,
            MediaMapper mapper,
            StorageProvider storageProvider,
            OwnershipValidator ownershipValidator
    ) {
        return new UploadMediaUseCase(
                repository,
                mapper,
                storageProvider,
                ownershipValidator
        );
    }
}
