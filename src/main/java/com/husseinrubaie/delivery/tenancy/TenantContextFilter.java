package com.husseinrubaie.delivery.tenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Guarantees the {@link TenantContext} never leaks across pooled request threads
 * by clearing it once each request completes.
 *
 * <p>The tenant is bound to the {@code public} schema by default. From Phase 1,
 * once a user is authenticated their tenant (delivery-company schema) is resolved
 * from the security principal and set here before the chain proceeds.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // TODO (Phase 1): resolve tenant from the authenticated principal and
            //  call TenantContext.setTenant(...) here for tenant-scoped users.
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
