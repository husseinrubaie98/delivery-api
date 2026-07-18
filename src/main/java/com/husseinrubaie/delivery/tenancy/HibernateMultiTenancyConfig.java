package com.husseinrubaie.delivery.tenancy;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Registers the schema-per-tenant infrastructure with Hibernate.
 *
 * <p>Since Hibernate 6, supplying a {@code MultiTenantConnectionProvider} and a
 * {@code CurrentTenantIdentifierResolver} is enough to enable multi-tenancy — no
 * explicit strategy setting is required. Both are injected into the JPA properties
 * as instances so they participate in the Spring context (the connection provider
 * wraps the application {@link DataSource}).</p>
 */
@Configuration
public class HibernateMultiTenancyConfig {

    @Bean
    public CurrentTenantResolver currentTenantResolver() {
        return new CurrentTenantResolver();
    }

    @Bean
    public SchemaMultiTenantConnectionProvider schemaMultiTenantConnectionProvider(DataSource dataSource) {
        return new SchemaMultiTenantConnectionProvider(dataSource);
    }

    @Bean
    public HibernatePropertiesCustomizer multiTenancyHibernateCustomizer(
            SchemaMultiTenantConnectionProvider connectionProvider,
            CurrentTenantResolver tenantResolver) {
        return properties -> {
            properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
            properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        };
    }
}
