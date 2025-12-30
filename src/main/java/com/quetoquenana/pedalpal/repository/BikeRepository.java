package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikeRepository extends JpaRepository<Bike, UUID>, BikeRepositoryCustom {
}

