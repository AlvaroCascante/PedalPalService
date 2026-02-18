package com.quetoquenana.pedalpal.presentation.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Helper for MockMvc request post-processing when you don't want annotations.
 */
public final class TestJwt {

    private TestJwt() {}

    public static JwtAuthenticationToken auth(UUID userId, String... roles) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("userId", userId.toString())
                .claim("name", "Test User")
                .subject("test")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        List<SimpleGrantedAuthority> authorities = java.util.Arrays.stream(roles)
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}

