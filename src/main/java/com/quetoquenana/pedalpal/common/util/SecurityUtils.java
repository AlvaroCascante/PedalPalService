package com.quetoquenana.pedalpal.common.util;

import com.quetoquenana.pedalpal.presentation.dto.util.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<SecurityUser> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();

        Jwt jwt = null;
        if (auth.getPrincipal() instanceof Jwt) {
            jwt = (Jwt) auth.getPrincipal();
        } else if (auth instanceof JwtAuthenticationToken) {
            jwt = ((JwtAuthenticationToken) auth).getToken();
        }

        return SecurityUser.fromJwtAndAuth(jwt, auth);
    }
}
