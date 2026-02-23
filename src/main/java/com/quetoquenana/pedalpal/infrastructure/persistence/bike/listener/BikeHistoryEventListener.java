package com.quetoquenana.pedalpal.infrastructure.persistence.bike.listener;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BikeHistoryEventListener {

    private final BikeHistoryRepository historyRepository;
    private final BikeMapper mapper;

    @Async
    @EventListener
    public void handle(BikeHistoryEvent event) {
        log.debug("Received BikeHistoryEvent: {}", event);
        try {
            BikeHistory model = mapper.toBikeHistory(event);
            historyRepository.save(model);
        } catch (Exception ex) {
            log.error("Failed to save bike history", ex);
        }
    }
}