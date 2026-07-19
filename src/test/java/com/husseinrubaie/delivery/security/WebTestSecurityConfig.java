package com.husseinrubaie.delivery.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Test-scoped identity for web-slice tests. {@link SecurityConfig} requires a
 * {@link UserDetailsService} (remember-me), which {@code @WebMvcTest} slices do
 * not auto-configure — import this class alongside {@code SecurityConfig}.
 */
@TestConfiguration
public class WebTestSecurityConfig {

    public static final String TEST_EMAIL = "owner@shop.example";
    public static final String TEST_PASSWORD = "Str0ngPass";

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(User.withUsername(TEST_EMAIL)
                .password(encoder.encode(TEST_PASSWORD))
                .roles("SHOP_OWNER")
                .build());
    }
}
