package com.quetoquenana.pedalpal.config;

import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.usecase.CreateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentStatusUseCase;
import com.quetoquenana.pedalpal.appointment.application.usecase.UpdateAppointmentUseCase;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.useCase.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

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
    public CreateAppointmentUseCase createCreateAppointmentUseCase(
            AppointmentMapper mapper,
            AppointmentRepository appointmentRepository,
            BikeRepository bikeRepository,
            ProductRepository productRepository
    ) {
        return new CreateAppointmentUseCase(
                mapper,
                appointmentRepository,
                bikeRepository,
                productRepository
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
            AppointmentRepository appointmentRepository,
            ProductRepository productRepository
    ) {
        return new UpdateAppointmentUseCase(
                mapper,
                appointmentRepository,
                productRepository
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