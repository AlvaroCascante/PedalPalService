package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentService;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        validateBikeExists(command.bikeId());

        Appointment appointment = mapper.toAppointment(command);

        if (command.requestedServices() != null) {
            for (CreateAppointmentCommand.ServiceCommandItem item : command.requestedServices()) {
                AppointmentService rs = snapshotRequestedService(item.productId());
                appointment.addRequestedService(rs);
            }
        }

        try {
            appointment = appointmentRepository.save(appointment);
            return mapper.toResult(appointment);
        } catch (RuntimeException ex) {
            log.error("RuntimeException creating appointment: {}", ex.getMessage());
            throw new BusinessException("appointment.creation.failed");
        }
    }

    private void validateBikeExists(UUID bikeId) {
        if (bikeId == null) {
            throw new BadRequestException("appointment.bikeId.required");
        }
        bikeRepository.getById(bikeId).orElseThrow(() -> new RecordNotFoundException("bike.not.found"));
    }

    private AppointmentService snapshotRequestedService(UUID productId) {
        if (productId == null) {
            throw new BadRequestException("appointment.requestedService.productId.required");
        }
        Product product = productRepository.getById(productId)
                .orElseThrow(() -> new RecordNotFoundException("product.not.found"));

        return AppointmentService.builder()
                .id(UUID.randomUUID())
                .product(product)
                .productNameSnapshot(product.getName())
                .priceSnapshot(product.getPrice())
                .build();
    }
}

