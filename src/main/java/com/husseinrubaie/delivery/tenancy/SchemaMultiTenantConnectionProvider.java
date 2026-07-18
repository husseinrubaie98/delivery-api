package com.husseinrubaie.delivery.tenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Schema-per-tenant connection provider.
 *
 * <p>Backed by a single pooled {@link DataSource}. For each borrowed connection it
 * sets the PostgreSQL {@code search_path} to the resolved tenant schema, so all SQL
 * issued during that unit of work targets the tenant's schema. The {@code search_path}
 * is reset to {@code public} before the connection returns to the pool, preventing a
 * tenant from leaking onto a reused connection.</p>
 */
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private static final Logger log = LoggerFactory.getLogger(SchemaMultiTenantConnectionProvider.class);

    private final transient DataSource dataSource;

    public SchemaMultiTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        setSchema(connection, TenantContext.DEFAULT_TENANT);
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        resetAndClose(connection);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = dataSource.getConnection();
        setSchema(connection, tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        resetAndClose(connection);
    }

    /**
     * Resets the {@code search_path} to the default schema and always closes the
     * connection. A failed reset is logged but never prevents the close — otherwise
     * the connection would leak out of the pool.
     */
    private void resetAndClose(Connection connection) throws SQLException {
        try {
            setSchema(connection, TenantContext.DEFAULT_TENANT);
        } catch (SQLException e) {
            log.warn("Failed to reset search_path before releasing connection; closing anyway", e);
        } finally {
            connection.close();
        }
    }

    private void setSchema(Connection connection, String schema) throws SQLException {
        // Schema names are validated against a strict allow-list to keep this safe.
        String safeSchema = TenantSchema.requireValid(schema);
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO \"" + safeSchema + "\"");
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return MultiTenantConnectionProvider.class.equals(unwrapType)
                || SchemaMultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) this;
        }
        throw new IllegalArgumentException("Cannot unwrap to " + unwrapType);
    }
}
