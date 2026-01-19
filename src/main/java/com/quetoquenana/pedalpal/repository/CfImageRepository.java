package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.local.CfImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CfImageRepository extends JpaRepository<CfImage, UUID> {

    List<CfImage> findByProvider(String provider);

    List<CfImage> findByOwnerId(UUID ownerId);

    Optional<CfImage> findByProviderAssetId(String providerAssetId);

    List<CfImage> findByContextCode(UUID contextCode);

    List<CfImage> findByReferenceId(UUID referenceId);
}

