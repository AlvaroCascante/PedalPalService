package com.quetoquenana.pedalpal.auditing;

import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    private final CurrentUserProvider currentUserProvider;

    public SpringSecurityAuditorAware(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        return currentUserProvider
                .getCurrentUser()
                .map(SecurityUser::userId);
    }
}