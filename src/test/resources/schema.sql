-- Test schema – identical to main schema.sql
CREATE TABLE IF NOT EXISTS countries (
    id VARCHAR(2) NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL,
    numeric_code VARCHAR(3), alpha3_code VARCHAR(3), eurozone BOOLEAN DEFAULT FALSE, sepa BOOLEAN DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS currencies (
    id VARCHAR(3) NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS silos (
    id VARCHAR(50) NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL, description VARCHAR(500),
    email VARCHAR(255), default_base_currency VARCHAR(3), default_credit_limit DECIMAL(19,2) DEFAULT 0,
    default_profit_share DECIMAL(5,4) DEFAULT 0, type VARCHAR(30)
);
CREATE TABLE IF NOT EXISTS corporations (
    id UUID NOT NULL PRIMARY KEY, name VARCHAR(255), code VARCHAR(50),
    incorporation_date DATE, incorporation_country VARCHAR(2), type VARCHAR(50), duplicates UUID
);
CREATE TABLE IF NOT EXISTS people (
    id UUID NOT NULL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), duplicates UUID
);
CREATE TABLE IF NOT EXISTS bank_accounts (
    id UUID NOT NULL PRIMARY KEY, beneficiary VARCHAR(255), beneficiary_address VARCHAR(500),
    nickname VARCHAR(255), iban VARCHAR(34), bic VARCHAR(11), account_number VARCHAR(50),
    national_bank_code VARCHAR(50), national_branch_code VARCHAR(50), national_clearing_code VARCHAR(50),
    currency VARCHAR(3), country VARCHAR(2)
);
CREATE TABLE IF NOT EXISTS customer_accounts (
    id UUID NOT NULL PRIMARY KEY, name VARCHAR(255), description VARCHAR(500), account_type VARCHAR(30),
    account_state VARCHAR(30), account_manager UUID, account_creation_time TIMESTAMP, silo VARCHAR(50)
);
CREATE TABLE IF NOT EXISTS corporation_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, resource UUID NOT NULL, version INT NOT NULL,
    name VARCHAR(255), code VARCHAR(50), incorporation_date DATE, incorporation_country VARCHAR(2),
    type VARCHAR(50), duplicates UUID
);
CREATE TABLE IF NOT EXISTS person_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, resource UUID NOT NULL, version INT NOT NULL,
    first_name VARCHAR(255), last_name VARCHAR(255), duplicates UUID
);
CREATE TABLE IF NOT EXISTS bank_account_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, resource UUID NOT NULL, version INT NOT NULL,
    beneficiary VARCHAR(255), beneficiary_address VARCHAR(500), nickname VARCHAR(255), iban VARCHAR(34),
    bic VARCHAR(11), account_number VARCHAR(50), national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50), national_clearing_code VARCHAR(50), currency VARCHAR(3), country VARCHAR(2)
);
CREATE TABLE IF NOT EXISTS beneficial_owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, account_id UUID NOT NULL, account_type VARCHAR(30) NOT NULL,
    owner_id UUID NOT NULL, owner_type VARCHAR(30) NOT NULL
);

