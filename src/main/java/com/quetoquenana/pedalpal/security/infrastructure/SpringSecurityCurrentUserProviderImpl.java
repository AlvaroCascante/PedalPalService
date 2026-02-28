package com.quetoquenana.pedalpal.security.infrastructure;

import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.security.domain.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_NAME;
import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_USER_ID;
import static com.quetoquenana.pedalpal.common.util.Constants.Roles.ROLE_ADMIN;

@Component
public class SpringSecurityCurrentUserProviderImpl implements CurrentUserProvider {

    @Override
    public Optional<SecurityUser> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }
        Jwt jwt = extractJwt(auth);
        return buildSecurityUser(jwt, auth);
    }

    private Jwt extractJwt(Authentication auth) {
        if (auth.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        if (auth instanceof JwtAuthenticationToken token) {
            return token.getToken();
        }
        return null;
    }

    private Optional<SecurityUser> buildSecurityUser(Jwt jwt, Authentication auth) {
        if (jwt == null) return Optional.empty();

        UUID userId = extractUserId(jwt);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()));

        return Optional.of(new SecurityUser(
                userId,
                auth.getName(),
                jwt.getClaimAsString(KEY_NAME),
                isAdmin
        ));
    }

    private UUID extractUserId(Jwt jwt) {
        try {
            return UUID.fromString(jwt.getClaimAsString(KEY_USER_ID));
        } catch (Exception e) {
            return null;
        }
    }
}
