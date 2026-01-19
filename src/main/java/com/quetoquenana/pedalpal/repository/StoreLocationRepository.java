package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.local.StoreLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreLocationRepository extends JpaRepository<StoreLocation, UUID> {

}

