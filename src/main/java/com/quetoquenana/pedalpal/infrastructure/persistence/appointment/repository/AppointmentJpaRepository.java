package com.quetoquenana.pedalpal.infrastructure.persistence.appointment.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {

    @Query("select a from AppointmentEntity a where a.bikeId = :bikeId and a.scheduledAt >= :now order by a.scheduledAt asc")
    List<AppointmentEntity> findUpcomingByBikeId(@Param("bikeId") UUID bikeId, @Param("now") Instant now);

    @Query("select a from AppointmentEntity a where a.bikeId = :bikeId and a.scheduledAt < :now order by a.scheduledAt desc")
    List<AppointmentEntity> findPastByBikeId(@Param("bikeId") UUID bikeId, @Param("now") Instant now);
}
