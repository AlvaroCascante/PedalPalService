package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.model.data.SystemCode;

import java.util.Optional;

public interface SystemCodeService {

    Optional<SystemCode> findByCategoryAndCode(String category, String code);
}

