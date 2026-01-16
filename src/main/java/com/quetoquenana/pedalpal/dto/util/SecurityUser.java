package com.quetoquenana.pedalpal.dto.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.JWTClaims.KEY_NAME;
import static com.quetoquenana.pedalpal.util.Constants.JWTClaims.KEY_USER_ID;

public record SecurityUser(
        UUID userId,
        String username,
        String name,
        boolean isAdmin
) {

    public static Optional<SecurityUser> fromJwtAndAuth(Jwt jwt, Authentication auth) {
        if (jwt == null || auth == null) return Optional.empty();

        String username = auth.getName(); // usually 'sub'
        String name = Optional.ofNullable(jwt.getClaimAsString(KEY_NAME)).orElse(username);

        UUID userId = null;
        String userIdClaim = jwt.getClaimAsString(KEY_USER_ID);
        if (userIdClaim != null) {
            try {
                userId = UUID.fromString(userIdClaim);
            } catch (IllegalArgumentException ignored) {
                // malformed UUID -> keep null
            }
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        return Optional.of(new SecurityUser(userId, username, name, isAdmin));
    }
}