package com.quetoquenana.pedalpal.appointment.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.infrastructure.persistence.entity.AppointmentEntity;
import com.quetoquenana.pedalpal.appointment.infrastructure.persistence.mapper.AppointmentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final AppointmentJpaRepository repository;

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = AppointmentEntityMapper.toEntity(appointment);
        entity = repository.save(entity);
        return AppointmentEntityMapper.toModel(entity);
    }

    @Override
    public Optional<Appointment> getById(UUID id) {
        return repository.findById(id).map(AppointmentEntityMapper::toModel);
    }

    @Override
    public List<Appointment> findUpcomingByBikeId(UUID bikeId, Instant now) {
        return repository.findUpcomingByBikeId(bikeId, now).stream()
                .map(AppointmentEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<Appointment> findPastByBikeId(UUID bikeId, Instant now) {
        return repository.findPastByBikeId(bikeId, now).stream()
                .map(AppointmentEntityMapper::toModel)
                .toList();
    }
}

