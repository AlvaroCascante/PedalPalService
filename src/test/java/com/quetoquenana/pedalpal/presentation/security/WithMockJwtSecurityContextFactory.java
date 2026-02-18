package com.quetoquenana.pedalpal.presentation.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Creates a JwtAuthenticationToken for {@link WithMockJwt}.
 */
public class WithMockJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwt> {

    @Override
    public org.springframework.security.core.context.SecurityContext createSecurityContext(WithMockJwt annotation) {
        org.springframework.security.core.context.SecurityContext context =
                org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();

        List<String> rolesClaim = Arrays.asList(annotation.roles());

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .subject(annotation.subject())
                .claim("userId", annotation.userId())
                .claim("name", annotation.name())
                // IMPORTANT: SecurityConfig expects roles in claim name 'roles'
                .claim("roles", rolesClaim)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // Authorities are still added directly for convenience (and for any code reading them directly)
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : annotation.roles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        AbstractAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
        authentication.setAuthenticated(true);
        context.setAuthentication(authentication);
        return context;
    }
}
