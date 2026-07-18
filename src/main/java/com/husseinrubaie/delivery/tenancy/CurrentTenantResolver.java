package com.husseinrubaie.delivery.tenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * Resolves the tenant Hibernate should use for the current unit of work by
 * reading the {@link TenantContext} of the executing thread. Defaults to the
 * shared {@code public} schema when no tenant is bound.
 */
public class CurrentTenantResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantContext.getTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
