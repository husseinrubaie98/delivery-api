package com.husseinrubaie.delivery.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * DEV-ONLY demo account so the login flow can be exercised before the real
 * identity backend (DP-531) exists. Never active outside the {@code dev}
 * profile; delete this class once users live in the database.
 *
 * <p>Credentials: {@code demo@delivery.local} / {@code Demo1234}</p>
 */
@Configuration
@Profile("dev")
public class DevUserConfig {

    public static final String DEMO_EMAIL = "demo@delivery.local";

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(User.withUsername(DEMO_EMAIL)
                .password(passwordEncoder.encode("Demo1234"))
                .roles("SHOP_OWNER")
                .build());
    }
}
