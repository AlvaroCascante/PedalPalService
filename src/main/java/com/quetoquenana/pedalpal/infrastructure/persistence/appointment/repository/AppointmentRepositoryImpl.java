package com.quetoquenana.pedalpal.infrastructure.persistence.appointment.repository;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.AppointmentEntityMapper;
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
    private final AppointmentEntityMapper mapper;

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = mapper.toEntity(appointment);
        entity = repository.save(entity);
        return mapper.toModel(entity);
    }

    @Override
    public Optional<Appointment> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Appointment> findUpcomingByBikeId(UUID bikeId, Instant now) {
        return repository.findUpcomingByBikeId(bikeId, now).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public List<Appointment> findPastByBikeId(UUID bikeId, Instant now) {
        return repository.findPastByBikeId(bikeId, now).stream()
                .map(mapper::toModel)
                .toList();
    }
}

