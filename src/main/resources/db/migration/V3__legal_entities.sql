-- V3 - Create legal entities tables with single-table inheritance

CREATE TABLE legal_entities (
    id UUID PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    corporation_type VARCHAR(255),
    duplicates UUID
);

CREATE INDEX idx_legal_entity_type ON legal_entities(entity_type);
CREATE INDEX idx_legal_entity_created ON legal_entities(created_at);

-- Audit tables for legal entities
CREATE TABLE person_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE corporation_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    corporation_type VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP NOT NULL
);
