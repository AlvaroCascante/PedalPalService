package com.quetoquenana.pedalpal.infrastructure.persistence.provider;

import com.quetoquenana.pedalpal.common.util.SecurityUtils;
import com.quetoquenana.pedalpal.common.util.SecurityUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        return SecurityUtils.getCurrentUser()
                .map(SecurityUser::userId);
    }
}