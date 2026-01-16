package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.SystemCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemCodeRepository extends JpaRepository<SystemCode, UUID> {

    Optional<SystemCode> findByCategoryAndCode(String category, String code);
}

