package com.quetoquenana.pedalpal.security.application;

import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;

import java.util.Optional;

public interface CurrentUserProvider {
    Optional<SecurityUser> getCurrentUser();
}
