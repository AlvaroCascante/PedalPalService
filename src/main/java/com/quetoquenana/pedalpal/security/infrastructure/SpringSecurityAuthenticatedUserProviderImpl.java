package com.quetoquenana.pedalpal.security.infrastructure;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_NAME;
import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_USER_ID;
import static com.quetoquenana.pedalpal.common.util.Constants.Roles.*;

@Component
public class SpringSecurityAuthenticatedUserProviderImpl implements AuthenticatedUserPort {

    @Override
    public Optional<AuthenticatedUser> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }
        Jwt jwt = extractJwt(auth);
        return buildUser(jwt, auth);
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

    private Optional<AuthenticatedUser> buildUser(Jwt jwt, Authentication auth) {
        if (jwt == null || auth == null) {
            return Optional.empty();
        }

        UUID userId = extractUserId(jwt);
        UserType userType;
        if (auth.getAuthorities().stream().anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()))) {
            userType = UserType.ADMIN;
        } else if (auth.getAuthorities().stream().anyMatch(a -> ROLE_USER.equals(a.getAuthority()))) {
            userType = UserType.CUSTOMER;
        } else if (auth.getAuthorities().stream().anyMatch(a -> ROLE_TECHNICIAN.equals(a.getAuthority()))) {
            userType = UserType.TECHNICIAN;
        } else {
            userType = UserType.UNKNOWN;
        }

        return Optional.of(new AuthenticatedUser(
                userId,
                auth.getName(),
                jwt.getClaimAsString(KEY_NAME),
                userType
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
