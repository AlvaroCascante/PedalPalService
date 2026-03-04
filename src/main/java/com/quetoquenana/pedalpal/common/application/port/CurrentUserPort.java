package com.quetoquenana.pedalpal.common.application.port;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;

import java.util.Optional;

/**
 * Port to access information about the currently authenticated user.
 * <p>
 * This port is defined in the application core so presentation/use cases don't depend on
 * Spring Security-specific types.
 */
public interface CurrentUserPort {

    /**
     * @return the currently authenticated user if present.
     */
    Optional<AuthenticatedUser> getCurrentUser();
}

