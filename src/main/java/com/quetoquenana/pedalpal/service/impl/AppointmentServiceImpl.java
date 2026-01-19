package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.local.Appointment;
import com.quetoquenana.pedalpal.model.local.Bike;
import com.quetoquenana.pedalpal.model.local.StoreLocation;
import com.quetoquenana.pedalpal.model.local.SystemCode;
import com.quetoquenana.pedalpal.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.service.AppointmentService;
import com.quetoquenana.pedalpal.service.BikeService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BikeService bikeService;
    private final SystemCodeService systemCodeService;
    private final StoreLocationRepository storeLocationRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, BikeService bikeService, SystemCodeService systemCodeService, StoreLocationRepository storeLocationRepository) {
        this.appointmentRepository = appointmentRepository;
        this.bikeService = bikeService;
        this.systemCodeService = systemCodeService;
        this.storeLocationRepository = storeLocationRepository;
    }

    @Override
    public Appointment findById(UUID id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("appointment.not.found", id));
    }

    @Override
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Bike bike = bikeService.findById(request.getBikeId());

        SystemCode status;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode("APPOINTMENT_STATUS", request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("appointment.status.not.found", request.getStatus()));
        } else {
            status = systemCodeService.findByCategoryAndCode("APPOINTMENT_STATUS", "SCHEDULED")
                    .orElseThrow(() -> new RecordNotFoundException("appointment.status.not.found", "SCHEDULED"));
        }

        StoreLocation storeLocation = null;
        if (request.getStoreLocationId() != null) {
            storeLocation = storeLocationRepository.findById(request.getStoreLocationId())
                    .orElseThrow(() -> new RecordNotFoundException("store.location.not.found", request.getStoreLocationId()));
        }

        Appointment appointment = Appointment.createFromRequest(request, bike, storeLocation, status);

        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setCreatedBy(user.username());
        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(user.username());

        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateAppointment(UUID id, UpdateAppointmentRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("appointment.not.found", id));

        SystemCode status = null;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode("APPOINTMENT_STATUS", request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("appointment.status.not.found", request.getStatus()));
        }

        StoreLocation storeLocation = null;
        if (request.getStoreLocationId() != null) {
            storeLocation = storeLocationRepository.findById(request.getStoreLocationId())
                    .orElseThrow(() -> new RecordNotFoundException("store.location.not.found", request.getStoreLocationId()));
        }

        appointment.updateFromRequest(request, status, storeLocation);

        appointment.setUpdatedAt(LocalDateTime.now());
        appointment.setUpdatedBy(user.username());

        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("appointment.not.found", id));
        appointmentRepository.delete(appointment);
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }
}
