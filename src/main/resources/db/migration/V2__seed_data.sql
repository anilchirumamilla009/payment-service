-- =====================================================
-- V2: Seed Reference Data
-- =====================================================

-- Currencies
INSERT INTO currencies (id, name) VALUES ('USD', 'United States Dollar');
INSERT INTO currencies (id, name) VALUES ('EUR', 'Euro');
INSERT INTO currencies (id, name) VALUES ('GBP', 'British Pound Sterling');
INSERT INTO currencies (id, name) VALUES ('CHF', 'Swiss Franc');
INSERT INTO currencies (id, name) VALUES ('JPY', 'Japanese Yen');
INSERT INTO currencies (id, name) VALUES ('CAD', 'Canadian Dollar');
INSERT INTO currencies (id, name) VALUES ('AUD', 'Australian Dollar');
INSERT INTO currencies (id, name) VALUES ('SGD', 'Singapore Dollar');
INSERT INTO currencies (id, name) VALUES ('HKD', 'Hong Kong Dollar');
INSERT INTO currencies (id, name) VALUES ('SEK', 'Swedish Krona');

-- Countries
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('US', 'United States of America', '840', 'USA', FALSE, FALSE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('GB', 'United Kingdom', '826', 'GBR', FALSE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('DE', 'Germany', '276', 'DEU', TRUE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('FR', 'France', '250', 'FRA', TRUE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('IE', 'Ireland', '372', 'IRL', TRUE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('CH', 'Switzerland', '756', 'CHE', FALSE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('JP', 'Japan', '392', 'JPN', FALSE, FALSE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('CA', 'Canada', '124', 'CAN', FALSE, FALSE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('AU', 'Australia', '036', 'AUS', FALSE, FALSE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('SG', 'Singapore', '702', 'SGP', FALSE, FALSE);

-- Silos
INSERT INTO silos (id, name, description, email, default_base_currency, default_credit_limit, default_profit_share, type)
VALUES ('TREASURY_01', 'Main Treasury', 'Primary treasury silo for liquidity management', 'treasury@techwave.com', 'USD', 1000000.0000, 0.1500, 'TREASURY');
INSERT INTO silos (id, name, description, email, default_base_currency, default_credit_limit, default_profit_share, type)
VALUES ('BU_EU_01', 'EU Business Unit', 'European business unit operations', 'eu-ops@techwave.com', 'EUR', 500000.0000, 0.2000, 'BUSINESS_UNIT');
INSERT INTO silos (id, name, description, email, default_base_currency, default_credit_limit, default_profit_share, type)
VALUES ('SUB_UK_01', 'UK Subsidiary', 'United Kingdom subsidiary operations', 'uk-sub@techwave.com', 'GBP', 250000.0000, 0.1000, 'SUBSIDIARY');

