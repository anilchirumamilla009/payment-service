package com.techwave.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * OWASP-aligned security configuration.
 * <ul>
 *   <li>Stateless session – no CSRF required for REST APIs</li>
 *   <li>Security headers: X-Content-Type-Options, X-Frame-Options, Content-Security-Policy,
 *       Strict-Transport-Security, Referrer-Policy, Permissions-Policy, X-XSS-Protection</li>
 *   <li>HTTP Basic authentication with Spring Security default user for demo</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API – disable CSRF
            .csrf(csrf -> csrf.disable())

            // Session management
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // OWASP security headers
            .headers(headers -> headers
                .contentTypeOptions(Customizer.withDefaults())                       // X-Content-Type-Options: nosniff
                .frameOptions(fo -> fo.deny())                                        // X-Frame-Options: DENY
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000))                                       // HSTS 1 year
                .referrerPolicy(rp -> rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .permissionsPolicy(pp -> pp.policy("geolocation=(), camera=(), microphone=()"))
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-ancestors 'none'"))
            )

            // Endpoint authorization
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // HTTP Basic for demo purposes
            .httpBasic(Customizer.withDefaults());

        // Allow H2 console frames in dev
        http.headers(h -> h.frameOptions(fo -> fo.sameOrigin()));

        return http.build();
    }
}

