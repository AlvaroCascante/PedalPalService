package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.RequestedServiceCommand;
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
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    private final ProductPackageRepository productPackageRepository;

    @Transactional
    public AppointmentResult execute(CreateAppointmentCommand command) {
        validate(command);

        try {
            Appointment appointment = mapper.toModel(command);
            List<RequestedService> services = snapshotRequestedServices(command.requestedServices());
            appointment.setRequestedServices(services);
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

    private List<RequestedService> snapshotRequestedServices(List<RequestedServiceCommand> requestedServices) {
        if (requestedServices == null || requestedServices.isEmpty()) {
            throw new BadRequestException("appointment.requestedServices.required");
        }

        List<RequestedService> services = new ArrayList<>();

        for (RequestedServiceCommand serviceCommand : requestedServices) {
            if (serviceCommand == null) {
                continue;
            }
            if (serviceCommand.serviceId() == null || serviceCommand.serviceType() == null) {
                log.warn("Requested service is missing serviceId/serviceType: {}", serviceCommand);
                continue;
            }

            switch (serviceCommand.serviceType()) {
                case PACKAGE -> services.addAll(snapshotPackageProducts(serviceCommand.serviceId()));
                case PRODUCT -> snapshotProduct(serviceCommand.serviceId()).ifPresent(services::add);
                case UNKNOWN -> log.warn(
                        "Requested service with id {} has unknown service type {}",
                        serviceCommand.serviceId(),
                        serviceCommand.serviceType()
                );
            }
        }

        if (services.isEmpty()) {
            // If the user requested services but none were valid/active, treat it like missing.
            throw new BadRequestException("appointment.requestedServices.required");
        }

        return services;
    }

    private List<RequestedService> snapshotPackageProducts(UUID packageId) {
        Optional<ProductPackage> optPackage = productPackageRepository.getByIdAndStatus(packageId, GeneralStatus.ACTIVE);
        if (optPackage.isEmpty()) {
            log.warn("Requested package id {} could not be added due to missing/INACTIVE package", packageId);
            return List.of();
        }

        Set<Product> products = optPackage.get().getProducts();
        if (products == null || products.isEmpty()) {
            log.warn("Requested package id {} has no products", packageId);
            return List.of();
        }

        // Snapshot each product inside the package via repository lookup so we store the current name/price.
        // If a product is no longer ACTIVE, it won't be included.
        return products.stream()
                .map(Product::getId)
                .distinct()
                .map(this::snapshotProduct)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<RequestedService> snapshotProduct(UUID productId) {
        Optional<Product> optProduct = productRepository.getByIdAndStatus(productId, GeneralStatus.ACTIVE);
        if (optProduct.isEmpty()) {
            log.warn("Requested product id {} could not be added due to missing/INACTIVE product", productId);
            return Optional.empty();
        }

        Product product = optProduct.get();
        return Optional.of(
                RequestedService.builder()
                        .serviceId(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build()
        );
    }

    private void validate(CreateAppointmentCommand command) {
        bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        if (command.requestedServices() == null || command.requestedServices().isEmpty()) {
            throw new BadRequestException("appointment.requestedServices.required");
        }
    }
}
