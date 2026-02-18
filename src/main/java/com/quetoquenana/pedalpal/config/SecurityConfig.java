package com.quetoquenana.pedalpal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_FACTORY_ALGORITHM;
import static com.quetoquenana.pedalpal.common.util.Constants.JWTClaims.KEY_ROLES;
import static com.quetoquenana.pedalpal.common.util.Constants.Roles.ROLE_PREFIX;


@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final String issuer;
    private final String jwkSetUri;
    private final String audience;

    public SecurityConfig(
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri,
            @Value("${security.jwt.aud}") String audience)
    {
        this.issuer = issuer;
        this.jwkSetUri = jwkSetUri;
        this.audience = audience;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName(KEY_ROLES);
        authoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtAuthConverter;
    }

    // Use JWK set URI and validate issuer + timestamps + audience + alg
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // Configure issuer validator + default timestamp validator
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();

        // Audience validator ensures token was intended for this resource server
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);

        // Algorithm validator (optional) - ensure token uses expected signing algorithm
        OAuth2TokenValidator<Jwt> algValidator = new AlgorithmValidator(KEY_FACTORY_ALGORITHM);

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withTimestamp, withIssuer, audienceValidator, algValidator);

        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    // Validates the 'aud' claim contains the expected audience
    static class AudienceValidator implements OAuth2TokenValidator<Jwt> {
        private final String audience;
        private final OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);

        AudienceValidator(String audience) {
            this.audience = audience;
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            List<String> audiences = token.getAudience();
            if (audiences != null && audiences.contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(error);
        }
    }

    // Simple validator that checks the JWT header 'alg' value (optional but can help prevent alg confusion attacks)
    static class AlgorithmValidator implements OAuth2TokenValidator<Jwt> {
        private final String expectedAlg;
        private final OAuth2Error error;

        AlgorithmValidator(String expectedAlg) {
            this.expectedAlg = expectedAlg;
            this.error = new OAuth2Error("invalid_token", "Unexpected signing algorithm", null);
        }

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            Object algObj = token.getHeaders().get("alg");
            if (algObj instanceof String && expectedAlg.equals(algObj)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}