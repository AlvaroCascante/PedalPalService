package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.local.LandingPageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LandingPageItemRepository extends JpaRepository<LandingPageItem, UUID> {
    Page<LandingPageItem> findByStatusId(UUID statusId, Pageable pageable);
}
