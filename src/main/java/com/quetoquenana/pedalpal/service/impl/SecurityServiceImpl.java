package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.service.SecurityService;
import com.quetoquenana.pedalpal.util.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Override
    public boolean validateOwnerId(String ownerId) {
        if (ownerId == null) return false; // Value coming from the request

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return false;

        // If user has ADMIN role, allow the action regardless of ownership
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> Constants.Roles.ROLE_ADMIN.equals(a.getAuthority()));
        if (isAdmin) return true;

        // Only users with USER role should be validated for ownership
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(a -> Constants.Roles.ROLE_USER.equals(a.getAuthority()));
        if (!isUser) return false;

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            if (jwt.getClaims().containsKey(Constants.JWTClaims.KEY_USER_ID)) {
                return ownerId.equals(jwt.getClaim(Constants.JWTClaims.KEY_USER_ID));
            }
        }
        return false;
    }
}
