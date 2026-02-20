package com.quetoquenana.pedalpal.security.application;

import java.util.Optional;

public interface CurrentUserProvider {
    Optional<SecurityUser> getCurrentUser();
}
