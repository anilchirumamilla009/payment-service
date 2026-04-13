-- V1 - Initialize schema (countries, currencies, silos)

CREATE TABLE countries (
    code VARCHAR(2) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    numeric_code VARCHAR(3) NOT NULL,
    alpha3_code VARCHAR(3) NOT NULL,
    is_eurozone BOOLEAN NOT NULL DEFAULT FALSE,
    is_sepa BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_country_alpha3 ON countries(alpha3_code);
CREATE INDEX idx_country_numeric ON countries(numeric_code);

CREATE TABLE currencies (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE silos (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    email VARCHAR(255),
    default_base_currency VARCHAR(3),
    default_credit_limit DECIMAL(20, 2),
    default_profit_share DECIMAL(3, 2),
    silo_type VARCHAR(50)
);
