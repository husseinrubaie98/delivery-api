package com.husseinrubaie.delivery.tenancy;

import java.util.regex.Pattern;

/**
 * Helpers for deriving and validating tenant schema identifiers.
 *
 * <p>Schema names are interpolated into {@code SET search_path} / {@code CREATE SCHEMA}
 * statements, so they must never come from unvalidated input. Every name is checked
 * against a strict allow-list pattern before use.</p>
 */
public final class TenantSchema {

    /** Prefix for delivery-company schemas, keeping them clearly namespaced. */
    public static final String PREFIX = "tenant_";

    /** Lower-case letters, digits and underscores; must start with a letter. Max 63 (Postgres limit). */
    private static final Pattern VALID = Pattern.compile("^[a-z][a-z0-9_]{0,62}$");

    private TenantSchema() {
    }

    /** Builds a schema name for a delivery company from its registry id, e.g. {@code tenant_42}. */
    public static String forCompanyId(long companyId) {
        return PREFIX + companyId;
    }

    /** Returns {@code true} if the identifier is a safe, well-formed schema name. */
    public static boolean isValid(String schema) {
        return schema != null && VALID.matcher(schema).matches();
    }

    /** Validates the identifier, throwing if it is not a safe schema name. */
    public static String requireValid(String schema) {
        if (!isValid(schema)) {
            throw new IllegalArgumentException("Illegal tenant schema name: " + schema);
        }
        return schema;
    }
}
