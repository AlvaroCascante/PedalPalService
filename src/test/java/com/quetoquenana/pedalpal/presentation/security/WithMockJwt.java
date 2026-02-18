package com.quetoquenana.pedalpal.presentation.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

/**
 * Test annotation that sets a JwtAuthenticationToken in the SecurityContext.
 *
 * It matches the expectations of {@link com.quetoquenana.pedalpal.common.util.SecurityUtils}
 * (JWT principal with a userId claim).
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithMockJwtSecurityContextFactory.class)
public @interface WithMockJwt {

    /** JWT claim "userId" */
    String userId();

    /** Spring Security roles WITHOUT the ROLE_ prefix (e.g. USER, ADMIN). */
    String[] roles() default {"USER"};

    /** JWT subject */
    String subject() default "test-user";

    /** JWT claim "name" */
    String name() default "Test User";
}

