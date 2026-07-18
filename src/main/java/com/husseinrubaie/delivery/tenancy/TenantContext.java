package com.husseinrubaie.delivery.tenancy;

/**
 * Holds the current tenant (PostgreSQL schema) for the executing thread.
 *
 * <p>Under the schema-per-tenant model, every request runs against exactly one
 * schema. Cross-cutting/shared work (login, registration, tenant provisioning,
 * platform-admin operations) runs against the shared {@link #DEFAULT_TENANT}
 * ({@code public}) schema.</p>
 *
 * <p>The value set here is read by the Hibernate {@code CurrentTenantIdentifierResolver}
 * and applied to the JDBC connection's {@code search_path} by the
 * {@code MultiTenantConnectionProvider}. Always clear the context at the end of a
 * request/unit of work to avoid leaking a tenant onto a pooled thread — prefer
 * {@link #runInTenant(String, Runnable)} or {@link #callInTenant} where possible.</p>
 */
public final class TenantContext {

    /** The shared schema holding the tenant registry, global identity and platform admins. */
    public static final String DEFAULT_TENANT = "public";

    private static final ThreadLocal<String> CURRENT = ThreadLocal.withInitial(() -> DEFAULT_TENANT);

    private TenantContext() {
    }

    public static void setTenant(String schema) {
        CURRENT.set(schema == null || schema.isBlank() ? DEFAULT_TENANT : schema);
    }

    public static String getTenant() {
        return CURRENT.get();
    }

    public static boolean isDefault() {
        return DEFAULT_TENANT.equals(getTenant());
    }

    public static void clear() {
        CURRENT.remove();
    }

    /** Runs the given action bound to {@code schema}, restoring the prior tenant afterwards. */
    public static void runInTenant(String schema, Runnable action) {
        String previous = getTenant();
        try {
            setTenant(schema);
            action.run();
        } finally {
            setTenant(previous);
        }
    }

    /** Executes the given supplier bound to {@code schema}, restoring the prior tenant afterwards. */
    public static <T> T callInTenant(String schema, java.util.function.Supplier<T> action) {
        String previous = getTenant();
        try {
            setTenant(schema);
            return action.get();
        } finally {
            setTenant(previous);
        }
    }
}
