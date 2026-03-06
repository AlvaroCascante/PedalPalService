package com.quetoquenana.pedalpal.bike.infrastructure.persistence.listener;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BikeHistoryEventListener {

    private final BikeHistoryRepository historyRepository;
    private final BikeMapper mapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BikeHistoryEvent event) {
        log.debug("Received BikeHistoryEvent: {}", event);
        try {
            BikeHistory model = mapper.toModel(event);
            historyRepository.save(model);
        } catch (DataAccessException ex) {
            log.error("Failed to save bike history", ex);
        }
    }
}