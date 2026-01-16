package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.BikeComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BikeComponentRepository extends JpaRepository<BikeComponent, UUID> {

}

