package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {

}

