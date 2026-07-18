package com.husseinrubaie.delivery.tenancy;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates and migrates the PostgreSQL schema that backs a single tenant
 * (delivery company).
 *
 * <p>Invoked during delivery-company onboarding (Phase 1). Creating the schema and
 * applying the tenant migration set ({@code db/migration/tenant}) makes the tenant's
 * tables exist before anyone signs in against that schema. The operation is
 * idempotent: re-running it simply applies any pending migrations.</p>
 */
@Component
public class TenantSchemaProvisioner {

    private static final Logger log = LoggerFactory.getLogger(TenantSchemaProvisioner.class);
    private static final String TENANT_MIGRATIONS = "classpath:db/migration/tenant";

    private final DataSource dataSource;

    public TenantSchemaProvisioner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Ensures the given tenant schema exists and is migrated to the latest version.
     *
     * @param schema a validated tenant schema name (see {@link TenantSchema})
     */
    public void provision(String schema) {
        TenantSchema.requireValid(schema);
        log.info("Provisioning tenant schema '{}'", schema);
        createSchemaIfAbsent(schema);
        migrate(schema);
        log.info("Tenant schema '{}' is ready", schema);
    }

    private void createSchemaIfAbsent(String schema) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create tenant schema '" + schema + "'", e);
        }
    }

    private void migrate(String schema) {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations(TENANT_MIGRATIONS)
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }
}
