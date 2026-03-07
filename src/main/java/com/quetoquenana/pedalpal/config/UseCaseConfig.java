package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.announcement.application.usecase.ActivateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.InactivateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.appointment.application.handler.CompletedUpdatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.application.handler.ConfirmCreatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.application.handler.InProgressUpdatesServiceOrderHandler;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.service.CompleteAppointmentFromServiceOrderService;
import com.quetoquenana.pedalpal.appointment.application.usecase.*;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.application.port.UploadMediaPort;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.port.CdnUrlProvider;
import com.quetoquenana.pedalpal.media.application.port.OwnershipValidationPort;
import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.media.application.useCase.ConfirmMediaUploadUseCase;
import com.quetoquenana.pedalpal.media.application.useCase.MediaUploadUseCase;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.serviceorder.application.handler.CompleteWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.handler.StartWorkHandler;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.AddServiceOrderCommentUseCase;
import com.quetoquenana.pedalpal.serviceorder.application.usecase.ChangeServiceOrderStatusUseCase;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.List;

@Configuration
public class UseCaseConfig {

    /*
     * Announcements Use Cases
     */
    @Bean
    public ActivateAnnouncementUseCase createActivateAnnouncementUseCase(
            AnnouncementRepository repository,
            AnnouncementMapper mapper
    ) {
        return new ActivateAnnouncementUseCase(
                repository,
                mapper
        );
    }

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
    public InactivateAnnouncementUseCase createInactivateAnnouncementUseCase(
            AnnouncementRepository repository,
            AnnouncementMapper mapper
    ) {
        return new InactivateAnnouncementUseCase(
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
    public ChangeAppointmentStatusUseCase createChangeAppointmentStatusUseCase(
            AppointmentMapper mapper,
            AppointmentRepository repository,
            AuthenticatedUserPort authenticatedUserPort,
            CompletedUpdatesServiceOrderHandler completedUpdatesServiceOrderHandler,
            ConfirmCreatesServiceOrderHandler confirmCreatesServiceOrderHandler,
            InProgressUpdatesServiceOrderHandler inProgressUpdatesServiceOrderHandler,
            Clock clock
    ) {
        return new ChangeAppointmentStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(
                        completedUpdatesServiceOrderHandler,
                        confirmCreatesServiceOrderHandler,
                        inProgressUpdatesServiceOrderHandler
                ),
                clock
        );
    }

    @Bean
    public CreateAppointmentUseCase createCreateAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository repository,
            AuthenticatedUserPort authenticatedUserPort,
            BikeRepository bikeRepository,
            ProductRepository productRepository,
            ProductPackageRepository productPackageRepository
    ) {
        return new CreateAppointmentUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                bikeRepository,
                productRepository,
                productPackageRepository
        );
    }

    @Bean
    public UpdateAppointmentUseCase createUpdateAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository repository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new UpdateAppointmentUseCase(
                mapper,
                repository,
                authenticatedUserPort
        );
    }

    /*
     * Bike Use Cases
     */
    @Bean
    public AddBikeComponentUseCase createAddBikeComponentUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            AuthenticatedUserPort authenticatedUserPort,
            BikeMapper mapper,
            BikeRepository repository,
            SystemCodeRepository systemCodeRepository
    ) {
        return new AddBikeComponentUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository,
                systemCodeRepository
        );
    }

    @Bean
    public CreateBikeUseCase createCreateBikeUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            AuthenticatedUserPort authenticatedUserPort,
            BikeMapper mapper,
            BikeRepository repository
    ) {
        return new CreateBikeUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository
        );
    }

    @Bean
    public ReplaceBikeComponentUseCase createReplaceBikeComponentUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            BikeMapper mapper,
            BikeRepository repository,
            SystemCodeRepository systemCodeRepository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new ReplaceBikeComponentUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository,
                systemCodeRepository
        );
    }

    @Bean
    public UpdateBikeStatusUseCase createUpdateBikeStatusUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            BikeMapper mapper,
            BikeRepository repository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new UpdateBikeStatusUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository
        );
    }

    @Bean
    public UpdateBikeUseCase createUpdateBikeUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            BikeMapper mapper,
            BikeRepository repository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new UpdateBikeUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository
        );
    }

    @Bean
    public UpdateBikeComponentUseCase createUpdateBikeComponentUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            BikeMapper mapper,
            BikeRepository repository,
            SystemCodeRepository systemCodeRepository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new UpdateBikeComponentUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository,
                systemCodeRepository
        );
    }

    @Bean
    public UpdateBikeComponentStatusUseCase createUpdateBikeComponentStatusUseCase(
            ApplicationEventPublisher applicationEventPublisher,
            BikeMapper mapper,
            BikeRepository repository,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new UpdateBikeComponentStatusUseCase(
                applicationEventPublisher,
                authenticatedUserPort,
                mapper,
                repository
        );
    }

    @Bean
    public CreateBikeUploadMediaUseCase createCreateBikeUploadMediaUseCase(
            BikeMapper mapper,
            BikeRepository repository,
            UploadMediaPort uploadMediaPort,
            AuthenticatedUserPort authenticatedUserPort
    ) {
        return new CreateBikeUploadMediaUseCase(
                authenticatedUserPort,
                mapper,
                repository,
                uploadMediaPort
        );
    }

    /*
     * Media Use Cases
     */
    @Bean
    public ConfirmMediaUploadUseCase createConfirmUploadUseCase(
            MediaRepository repository,
            MediaMapper mapper,
            CdnUrlProvider cdnUrlProvider
    ) {
        return new ConfirmMediaUploadUseCase(
                repository,
                mapper,
                cdnUrlProvider
        );
    }

    @Bean
    public MediaUploadUseCase createGenerateUploadUrlUseCase(
            MediaRepository repository,
            MediaMapper mapper,
            StorageProvider storageProvider,
            OwnershipValidationPort ownershipValidationPort,
            AuthenticatedUserPort authenticatedUserPort,
            @Value("${app.media.default-provider}")
            String defaultStorageProvider
    ) {
        return new MediaUploadUseCase(
                repository,
                mapper,
                storageProvider,
                ownershipValidationPort,
                authenticatedUserPort,
                defaultStorageProvider

        );
    }

    /*
     * Service Order Use Cases
     */
    @Bean
    public ChangeServiceOrderStatusUseCase createChangeServiceOrderStatusUseCase(
            ServiceOrderMapper mapper,
            ServiceOrderRepository repository,
            AuthenticatedUserPort authenticatedUserPort,
            StartWorkHandler startWorkHandler,
            CompleteWorkHandler completeWorkHandler
    ) {
        return new ChangeServiceOrderStatusUseCase(
                mapper,
                repository,
                authenticatedUserPort,
                List.of(
                        startWorkHandler,
                        completeWorkHandler
                )
        );
    }

    @Bean
    public CompleteAppointmentFromServiceOrderService createCompleteAppointmentFromServiceOrderService(
            AppointmentRepository appointmentRepository
    ) {
        return new CompleteAppointmentFromServiceOrderService(appointmentRepository);
    }

    @Bean
    public AddServiceOrderCommentUseCase createAddServiceOrderCommentUseCase(
            AuthenticatedUserPort authenticatedUserPort,
            ServiceOrderRepository repository,
            ServiceOrderCommentRepository commentRepository,
            ServiceOrderMapper mapper,
            Clock clock
    ) {
        return new AddServiceOrderCommentUseCase(
                authenticatedUserPort,
                repository,
                commentRepository,
                mapper,
                clock
        );
    }
}
