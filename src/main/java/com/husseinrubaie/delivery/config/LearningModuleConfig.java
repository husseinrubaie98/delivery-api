package com.husseinrubaie.delivery.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Opt-in wiring for Hussein's Spring Boot learning/reference code.
 *
 * <p>The {@code com.husseinrubaie.learning} package is intentionally excluded from
 * the platform's default component scan so its demo endpoints (e.g. a controller
 * mapping GET {@code "/"}) don't collide with the real application. Activate it with
 * the {@code learning} profile when you want to experiment:</p>
 *
 * <pre>./gradlew bootRun --args='--spring.profiles.active=dev,learning'</pre>
 */
@Configuration
@Profile("learning")
@ComponentScan("com.husseinrubaie.learning")
public class LearningModuleConfig {
}
