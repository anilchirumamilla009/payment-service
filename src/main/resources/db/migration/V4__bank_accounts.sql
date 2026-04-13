-- V4 - Create bank accounts tables with beneficial owners and audits

CREATE TABLE bank_accounts (
    id UUID PRIMARY KEY,
    beneficiary VARCHAR(255),
    beneficiary_address TEXT,
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(255),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL
);

CREATE INDEX idx_bank_account_iban ON bank_accounts(iban);
CREATE INDEX idx_bank_account_country ON bank_accounts(country);

CREATE TABLE bank_account_beneficial_owners (
    bank_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (bank_account_id, legal_entity_id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id),
    FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);

CREATE INDEX idx_ba_bo_legal_entity ON bank_account_beneficial_owners(legal_entity_id);

CREATE TABLE bank_account_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    beneficiary VARCHAR(255),
    beneficiary_address TEXT,
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(255),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    created_at TIMESTAMP NOT NULL
);
