package com.quetoquenana.pedalpal.auditing;

import com.quetoquenana.pedalpal.common.application.port.CurrentUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    private final CurrentUserPort currentUserPort;

    public SpringSecurityAuditorAware(CurrentUserPort currentUserPort) {
        this.currentUserPort = currentUserPort;
    }

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        return currentUserPort.getCurrentUser().map(AuthenticatedUser::userId);
    }
}