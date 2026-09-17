CREATE TABLE organizations (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL UNIQUE,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    status      VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    deleted_at  TIMESTAMP(6)
);

CREATE TABLE roles (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE management_users (
    id              BIGSERIAL PRIMARY KEY,
    organization_id BIGINT UNIQUE REFERENCES organizations(id),
    role_id         BIGINT NOT NULL REFERENCES roles(id),
    username        VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    enabled         BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP(6) NOT NULL,
    updated_at      TIMESTAMP(6) NOT NULL
);

CREATE TABLE api_routes (
    id                      BIGSERIAL PRIMARY KEY,
    organization_id         BIGINT NOT NULL REFERENCES organizations(id),
    route_id                VARCHAR(100) NOT NULL,
    name                    VARCHAR(200) NOT NULL,
    path_pattern            VARCHAR(500) NOT NULL,
    target_base_url         VARCHAR(1000) NOT NULL,
    target_path_prefix      VARCHAR(500) NOT NULL,
    requests_per_minute     INTEGER,
    response_timeout_ms     INTEGER NOT NULL,
    idempotency_enabled     BOOLEAN NOT NULL DEFAULT FALSE,
    active                  BOOLEAN NOT NULL DEFAULT FALSE,
    configuration_version   BIGINT NOT NULL,
    created_at              TIMESTAMP(6) NOT NULL,
    updated_at              TIMESTAMP(6) NOT NULL,
    UNIQUE (organization_id, route_id)
);

CREATE TABLE route_allowed_methods (
    route_pk    BIGINT NOT NULL REFERENCES api_routes(id) ON DELETE CASCADE,
    http_method VARCHAR(10) NOT NULL,
    PRIMARY KEY (route_pk, http_method)
);

CREATE TABLE route_header_rules (
    id              BIGSERIAL PRIMARY KEY,
    route_pk        BIGINT NOT NULL REFERENCES api_routes(id) ON DELETE CASCADE,
    direction       VARCHAR(20) NOT NULL,
    action          VARCHAR(20) NOT NULL,
    header_name     VARCHAR(200) NOT NULL,
    header_value    VARCHAR(2000),
    created_at      TIMESTAMP(6) NOT NULL,
    updated_at      TIMESTAMP(6) NOT NULL
);

CREATE TABLE route_configuration_versions (
    id                      BIGSERIAL PRIMARY KEY,
    organization_id         BIGINT NOT NULL REFERENCES organizations(id),
    route_id                VARCHAR(100) NOT NULL,
    version_number          BIGINT NOT NULL,
    change_type             VARCHAR(30) NOT NULL,
    configuration_snapshot  TEXT NOT NULL,
    created_at              TIMESTAMP(6) NOT NULL,
    UNIQUE (organization_id, route_id, version_number)
);

CREATE TABLE control_plane_audit_records (
    id              BIGSERIAL PRIMARY KEY,
    organization_id BIGINT REFERENCES organizations(id),
    actor           VARCHAR(100) NOT NULL,
    action          VARCHAR(100) NOT NULL,
    resource_type   VARCHAR(100) NOT NULL,
    resource_id     VARCHAR(100) NOT NULL,
    correlation_id  VARCHAR(100),
    duration_ms     BIGINT NOT NULL,
    result          VARCHAR(20) NOT NULL,
    details         VARCHAR(2000),
    created_at      TIMESTAMP(6) NOT NULL
);
