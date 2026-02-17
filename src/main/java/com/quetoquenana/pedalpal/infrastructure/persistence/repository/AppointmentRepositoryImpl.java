package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl  {

    private final AppointmentJpaRepository appointmentJpaRepository;

    /*@Override
    public Appointment save(Appointment appointment) {
        return jpaAppointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Appointment> getAll() {
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public List<Appointment> findByBikeId(UUID bikeId) {
        Pageable pageable = PageRequest.of(0, MAINTENANCE_SUGGESTIONS_HISTORY_SIZE);
        return jpaAppointmentRepository.findByBikeIdOrderByDateDesc(bikeId, pageable);
    }*/
}
