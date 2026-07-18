package com.husseinrubaie.delivery.common.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Supplies the current principal's username for JPA auditing
 * ({@code @CreatedBy} / {@code @LastModifiedBy}).
 *
 * <p>Falls back to {@code "system"} for unauthenticated actions (e.g. background
 * jobs, tenant provisioning, migrations).</p>
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    static final String SYSTEM = "system";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of(SYSTEM);
        }
        return Optional.of(authentication.getName());
    }
}
