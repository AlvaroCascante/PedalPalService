package com.quetoquenana.pedalpal.systemCode.application.port;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;

import java.util.List;
import java.util.UUID;

/**
 * Application port for read-only SystemCode queries used by other modules.
 */
public interface SystemCodeQueryPort {

    /**
     * Retrieves a system code by id.
     */
    SystemCodeResult getById(UUID id);

    /**
     * Retrieves system codes by category and status.
     */
    List<SystemCodeResult> getByCategoryAndStatus(String category, GeneralStatus status);
}

