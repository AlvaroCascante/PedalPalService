package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {
}

