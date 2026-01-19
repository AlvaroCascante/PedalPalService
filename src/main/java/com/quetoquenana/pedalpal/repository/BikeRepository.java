package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeRepository extends JpaRepository<Bike, UUID> {

    @Query("select b from Bike b join b.status s ON s.code in :codes where b.ownerId = :ownerId ")
    List<Bike> findByOwnerIdAndStatusCodes(@Param("ownerId") UUID ownerId, @Param("codes") List<String> codes);

    List<Bike> findByOwnerId(UUID ownerId);

    Optional<Bike> findByIdAndOwnerId(UUID id, UUID ownerId);
}
