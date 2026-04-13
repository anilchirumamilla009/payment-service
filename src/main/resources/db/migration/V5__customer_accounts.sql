-- V5 - Create customer accounts tables with beneficial owners

CREATE TABLE customer_accounts (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    account_type VARCHAR(50) NOT NULL,
    account_state VARCHAR(50) NOT NULL,
    account_manager_id UUID,
    account_creation_time TIMESTAMP NOT NULL,
    silo_id VARCHAR(100),
    version BIGINT NOT NULL
);

CREATE INDEX idx_cust_account_silo ON customer_accounts(silo_id);
CREATE INDEX idx_cust_account_manager ON customer_accounts(account_manager_id);

CREATE TABLE customer_account_beneficial_owners (
    customer_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (customer_account_id, legal_entity_id),
    FOREIGN KEY (customer_account_id) REFERENCES customer_accounts(id),
    FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);
