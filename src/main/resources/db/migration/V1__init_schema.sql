-- =====================================================
-- V1: Initial Schema for Payment Service
-- =====================================================

-- Countries table
CREATE TABLE countries (
    id VARCHAR(2) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    numeric_code VARCHAR(3),
    alpha3_code VARCHAR(3),
    eurozone BOOLEAN DEFAULT FALSE,
    sepa BOOLEAN DEFAULT FALSE
);

-- Currencies table
CREATE TABLE currencies (
    id VARCHAR(3) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Silos table
CREATE TABLE silos (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    email VARCHAR(255),
    default_base_currency VARCHAR(3),
    default_credit_limit DECIMAL(19,4) DEFAULT 0.0,
    default_profit_share DECIMAL(5,4) DEFAULT 0.0,
    type VARCHAR(50),
    CONSTRAINT fk_silo_currency FOREIGN KEY (default_base_currency) REFERENCES currencies(id)
);

-- Legal Entities table (Single Table Inheritance for Person and Corporation)
CREATE TABLE legal_entities (
    id UUID NOT NULL PRIMARY KEY,
    resource_type VARCHAR(50) NOT NULL,
    -- Person fields
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    -- Corporation fields
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    type VARCHAR(100),
    -- Common fields
    duplicates UUID,
    -- Audit versioning
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_legal_entities_resource_type ON legal_entities(resource_type);
CREATE INDEX idx_corporation_country_code ON legal_entities(incorporation_country, code);

-- Bank Accounts table
CREATE TABLE bank_accounts (
    id UUID NOT NULL PRIMARY KEY,
    beneficiary VARCHAR(255),
    beneficiary_address VARCHAR(500),
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(50),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customer Accounts table
CREATE TABLE customer_accounts (
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(1000),
    account_type VARCHAR(50),
    account_state VARCHAR(50),
    account_manager UUID,
    account_creation_time TIMESTAMP,
    silo VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Person Audit Trail table
CREATE TABLE person_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource UUID NOT NULL,
    version INTEGER NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_person_audit_resource FOREIGN KEY (resource) REFERENCES legal_entities(id)
);

CREATE INDEX idx_person_audits_resource ON person_audits(resource);

-- Corporation Audit Trail table
CREATE TABLE corporation_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource UUID NOT NULL,
    version INTEGER NOT NULL,
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    type VARCHAR(100),
    duplicates UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_corporation_audit_resource FOREIGN KEY (resource) REFERENCES legal_entities(id)
);

CREATE INDEX idx_corporation_audits_resource ON corporation_audits(resource);

-- Bank Account Audit Trail table
CREATE TABLE bank_account_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource UUID NOT NULL,
    version INTEGER NOT NULL,
    beneficiary VARCHAR(255),
    beneficiary_address VARCHAR(500),
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(50),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bank_account_audit_resource FOREIGN KEY (resource) REFERENCES bank_accounts(id)
);

CREATE INDEX idx_bank_account_audits_resource ON bank_account_audits(resource);

-- Bank Account Beneficial Owners (Many-to-Many)
CREATE TABLE bank_account_beneficial_owners (
    bank_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    PRIMARY KEY (bank_account_id, legal_entity_id),
    CONSTRAINT fk_babo_bank_account FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
    CONSTRAINT fk_babo_legal_entity FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);

-- Customer Account Beneficial Owners (Many-to-Many)
CREATE TABLE customer_account_beneficial_owners (
    customer_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    PRIMARY KEY (customer_account_id, legal_entity_id),
    CONSTRAINT fk_cabo_customer_account FOREIGN KEY (customer_account_id) REFERENCES customer_accounts(id),
    CONSTRAINT fk_cabo_legal_entity FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);

