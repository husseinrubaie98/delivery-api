package com.husseinrubaie.delivery.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Baseline web security for the platform.
 *
 * <p>The UI is server-rendered, so authentication is <strong>session-based form
 * login</strong> (not JWT). Public pages, static assets and health/info endpoints
 * are open; everything else requires authentication. Real users, roles and a custom
 * login page arrive in Phase 1 (DP-531); this class establishes the shape.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Application-wide password hashing. BCrypt is the sensible default. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/error",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico",
                                "/actuator/health", "/actuator/info")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
    }
}
