package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.repository.LandingPageItemRepository;
import com.quetoquenana.pedalpal.service.LandingItemService;
import org.springframework.stereotype.Service;

@Service
public class LandingItemServiceImpl implements LandingItemService {

    private final LandingPageItemRepository repository;

    public LandingItemServiceImpl(LandingPageItemRepository repository) {
        this.repository = repository;
    }
}
