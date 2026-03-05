package com.quetoquenana.pedalpal.systemCode.infrastructure.adapter;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.application.port.SystemCodeQueryPort;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Infrastructure adapter exposing SystemCode queries via the application port.
 */
@Component
@RequiredArgsConstructor
class SystemCodeQueryPortAdapter implements SystemCodeQueryPort {

    private final SystemCodeQueryService queryService;

    /**
     * Retrieves a system code by id.
     */
    @Override
    public SystemCodeResult getById(UUID id) {
        return queryService.getById(id);
    }

    /**
     * Retrieves system codes by category and status.
     */
    @Override
    public List<SystemCodeResult> getByCategoryAndStatus(String category, GeneralStatus status) {
        return queryService.getByCategoryAndStatus(category, status);
    }
}
