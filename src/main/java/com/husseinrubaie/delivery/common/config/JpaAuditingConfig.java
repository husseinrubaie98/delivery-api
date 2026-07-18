package com.husseinrubaie.delivery.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing so {@code @CreatedDate}, {@code @LastModifiedDate},
 * {@code @CreatedBy} and {@code @LastModifiedBy} on {@code BaseEntity} are
 * populated automatically. The auditor is resolved by
 * {@link SpringSecurityAuditorAware}.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class JpaAuditingConfig {
}
