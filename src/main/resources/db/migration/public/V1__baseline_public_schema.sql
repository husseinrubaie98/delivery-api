-- =====================================================================
-- Public (shared) schema baseline.
--
-- The public schema holds cross-tenant data that must be reachable before a
-- tenant is known: the delivery-company (tenant) registry, and later the global
-- identity/login directory and platform admins (added in Phase 1).
--
-- Each delivery company additionally gets its own schema (tenant_<id>) created
-- and migrated at onboarding time from db/migration/tenant.
-- =====================================================================

-- ---------------------------------------------------------------------
-- delivery_company: the tenant registry.
-- One row per delivery company; schema_name maps the tenant to its schema.
-- ---------------------------------------------------------------------
CREATE TABLE delivery_company (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    slug        VARCHAR(80)  NOT NULL,
    schema_name VARCHAR(63)  NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    version     BIGINT       NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    CONSTRAINT uq_delivery_company_slug        UNIQUE (slug),
    CONSTRAINT uq_delivery_company_schema_name UNIQUE (schema_name),
    CONSTRAINT ck_delivery_company_status
        CHECK (status IN ('PENDING', 'ACTIVE', 'SUSPENDED'))
);

COMMENT ON TABLE delivery_company IS 'Tenant registry: one row per delivery company; maps a tenant to its PostgreSQL schema.';
COMMENT ON COLUMN delivery_company.schema_name IS 'Name of the tenant''s dedicated schema, e.g. tenant_42.';
COMMENT ON COLUMN delivery_company.status IS 'PENDING (awaiting activation), ACTIVE, or SUSPENDED.';

CREATE INDEX ix_delivery_company_status ON delivery_company (status);
