package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.announcement.application.usecase.CreateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementStatusUseCase;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementUseCase;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.appointment.application.usecase.ConfirmAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceOrder.repository.ServiceOrderRepository;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
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
            AnnouncementRepository repository
    ) {
        return new CreateAnnouncementUseCase(
                mapper,
                repository
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
    public ConfirmAppointmentUseCase createConfirmAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository appointmentRepository,
            ServiceOrderMapper serviceOrderMapper,
            ServiceOrderRepository serviceOrderRepository
    ) {
        return new ConfirmAppointmentUseCase(
                mapper,
                appointmentRepository,
                serviceOrderMapper,
                serviceOrderRepository
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
}
