package com.quetoquenana.pedalpal.auditing.provider;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    private final AuthenticatedUserPort authenticatedUserPort;

    public SpringSecurityAuditorAware(AuthenticatedUserPort authenticatedUserPort) {
        this.authenticatedUserPort = authenticatedUserPort;
    }

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        return authenticatedUserPort.getAuthenticatedUser().map(AuthenticatedUser::userId);
    }
}