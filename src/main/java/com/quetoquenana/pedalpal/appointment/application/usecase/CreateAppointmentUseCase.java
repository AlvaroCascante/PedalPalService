package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.RequestedService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Use case for creating a new appointment.
 * It validates the input, checks if the bike belongs to the authenticated user,
 * Check that at least one requested service is provided, and that the requested services are valid.
 * Snapshots the requested services, get the product name and price to be  preserved in case of products updates
 * It also handles exceptions and logs errors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final BikeRepository bikeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public AppointmentResult execute(CreateAppointmentCommand command) {
        validate(command);

        try {
            Appointment appointment = mapper.toModel(command);
            appointment.setRequestedServices(validateRequestedServices(command.requestedServices()));
            appointment = appointmentRepository.save(appointment);
            return mapper.toResult(appointment);
        } catch (BadRequestException | RecordNotFoundException ex) {
            log.error("Error creating appointment: {}", ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException creating appointment: {}", ex.getMessage());
            throw new BusinessException("appointment.creation.failed");
        }
    }

    private List<RequestedService> validateRequestedServices(List<CreateAppointmentCommand.RequestedServiceCommand> requestedServices) {
        List<RequestedService> services = new ArrayList<>();

        requestedServices.forEach(serviceCommand -> {
            RequestedService service = snapshotRequestedService(serviceCommand.productId());
            if (service != null) {
                services.add(service);
            } else {
                log.warn("Requested service with product id {} could not be added due to missing product", serviceCommand.productId());
            }
        });

        if (services.isEmpty()) {
            throw new BadRequestException("ppointment.requestedServices.required");
        }
        return services;
    }

    private RequestedService snapshotRequestedService(UUID productId) {
        Optional<Product> otpProduct = productRepository.getByIdAndStatus(productId, GeneralStatus.ACTIVE);
        if (otpProduct.isPresent()) {
            Product product = otpProduct.get();
            return RequestedService.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();
        }
        return null;
    }

    private void validate(CreateAppointmentCommand command) {
        bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        if (command.requestedServices() == null || command.requestedServices().isEmpty()) {
            throw new BadRequestException("appointment.requestedServices.required");
        }
    }
}
