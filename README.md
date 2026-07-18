# Delivery Platform

Multi-tenant SaaS for delivery operations, connecting **online shops**, **delivery
companies**, and their **clients**. Each order is tracked through an 8-state lifecycle
(Created → Packaged → Sent → Driver → Client Received → Money Received → Shop Received /
Complete, plus Canceled).

Backend: **Spring Boot 4 / Java 21**. UI: server-rendered **Thymeleaf + FlyonUI**
(Tailwind CSS v4). Persistence: **PostgreSQL** with **schema-per-tenant** multi-tenancy.

## Architecture at a glance

- **Multi-tenancy — schema per tenant.** A shared `public` schema holds the tenant
  registry (`delivery_company`), the global identity/login directory, and platform
  admins. Every delivery company gets its own schema (`tenant_<id>`) with all its
  business data. The tenant is resolved per request and applied by switching the
  PostgreSQL `search_path`. See `com.husseinrubaie.delivery.tenancy`.
- **Migrations — Flyway.** Boot runs `db/migration/public` on startup; each tenant
  schema is created and migrated from `db/migration/tenant` at onboarding time
  (`TenantSchemaProvisioner`). Hibernate never emits DDL (`ddl-auto: none`).
- **Security.** Session-based form login (Spring Security). BCrypt password hashing.
- **Frontend build.** Tailwind v4 + FlyonUI compiled by a Gradle-wired Node toolchain
  (`com.github.node-gradle.node`) from `src/main/frontend/` into generated static
  resources. No CDN, no committed build output.

## Prerequisites

- JDK 21 (the Gradle toolchain also enforces this)
- PostgreSQL 14+ running locally
- Docker (only for the Testcontainers integration tests)

Node.js is **not** required to be installed globally — the build downloads a pinned
version itself.

## Database setup (one time)

```sql
CREATE ROLE delivery_app WITH LOGIN PASSWORD 'delivery_app_pw';
CREATE DATABASE delivery_platform OWNER delivery_app;
GRANT ALL PRIVILEGES ON DATABASE delivery_platform TO delivery_app;
```

Adjust the credentials to taste and mirror them in `src/main/resources/application.yml`
(the `dev` profile) or override via `DB_URL` / `DB_USERNAME` / `DB_PASSWORD`.

## Running

```bash
./gradlew bootRun                 # starts on http://localhost:8090  (dev profile)
```

The Tailwind/FlyonUI CSS is compiled automatically as part of the build. To rebuild
styles on the fly while editing templates:

```bash
cd src/main/frontend && npm run watch:css
```

### Useful commands

| Command | Purpose |
| --- | --- |
| `./gradlew bootRun` | Run the app (dev profile) |
| `./gradlew build` | Full build + tests (tests need Docker) |
| `./gradlew buildCss` | Compile the FlyonUI stylesheet only |
| `./gradlew bootRun --args='--spring.profiles.active=dev,learning'` | Also expose the Spring Boot learning/reference endpoints |

## Project layout

```
src/main/java/com/husseinrubaie/delivery/
  common/      base entity, auditing, exceptions, error handling
  config/      opt-in learning module wiring
  security/    Spring Security configuration
  tenancy/     schema-per-tenant infrastructure
  web/         public web controllers
  platform/    shared-schema domain (tenant registry, identity — Phase 1+)
src/main/frontend/        Tailwind v4 + FlyonUI sources (app.css, package.json)
src/main/resources/
  db/migration/public/    shared-schema migrations
  db/migration/tenant/    per-tenant schema migrations (Phase 1+)
  templates/              Thymeleaf views
com/husseinrubaie/learning/   Spring Boot learning/reference code (activate via profile)
```

## Development workflow

The platform is built **phase by phase** against the JIRA project **DP** (Delivery
Platform), one epic or story at a time.
