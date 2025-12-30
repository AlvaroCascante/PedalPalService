package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.LandingCategory;
import com.quetoquenana.pedalpal.model.LandingItem;
import com.quetoquenana.pedalpal.model.LandingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LandingItemRepository extends JpaRepository<LandingItem, UUID> {
    Page<LandingItem> findByStatus(LandingStatus status, Pageable pageable);
    Page<LandingItem> findByCategory(LandingCategory category, Pageable pageable);
}
