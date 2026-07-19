package com.husseinrubaie.delivery.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

/**
 * Baseline web security for the platform.
 *
 * <p>The UI is server-rendered, so authentication is <strong>session-based form
 * login</strong> (not JWT) against the custom login page at {@code /login}, using
 * the user's email as the username. Public pages, static assets and health/info
 * endpoints are open; everything else requires authentication. Real users and
 * roles arrive with the identity backend of Phase 1 (DP-531).</p>
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
                                "/register", "/register/**",
                                "/verify-email",
                                // formLogin().permitAll() matches /login and /login?error by
                                // exact URL only; this covers variants like /login?logout.
                                "/login",
                                "/forgot-password", "/forgot-password/**",
                                "/reset-password", "/reset-password/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico",
                                "/actuator/health", "/actuator/info")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        // The UI signs users in with their email address.
                        .usernameParameter("email")
                        .permitAll())
                // Remember-me cookies survive the session for 30 days (DP-566).
                // No fixed key yet: tokens are invalidated by an app restart until a
                // configured secret arrives with the identity backend story.
                .rememberMe(remember -> remember
                        .tokenValiditySeconds((int) Duration.ofDays(30).toSeconds()))
                .logout(Customizer.withDefaults());
        return http.build();
    }
}
