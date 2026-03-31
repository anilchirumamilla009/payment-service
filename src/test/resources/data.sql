-- Test seed data
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('GB', 'United Kingdom', '826', 'GBR', FALSE, TRUE);
INSERT INTO countries (id, name, numeric_code, alpha3_code, eurozone, sepa) VALUES ('US', 'United States', '840', 'USA', FALSE, FALSE);
INSERT INTO currencies (id, name) VALUES ('EUR', 'Euro');
INSERT INTO currencies (id, name) VALUES ('USD', 'US Dollar');
INSERT INTO silos (id, name, description, email, default_base_currency, default_credit_limit, default_profit_share, type) VALUES ('treasury-core', 'Treasury Core', 'Central treasury operating silo', 'treasury-core@techwave.com', 'EUR', 1000000.00, 0.25, 'TREASURY');
INSERT INTO corporations (id, name, code, incorporation_date, incorporation_country, type, duplicates) VALUES ('11111111-1111-1111-1111-111111111111', 'Cornerstone FX Ltd', 'CFS-UK', '2017-01-15', 'GB', 'FINTECH', NULL);
INSERT INTO people (id, first_name, last_name, duplicates) VALUES ('22222222-2222-2222-2222-222222222222', 'Stephen', 'Flynn', NULL);
INSERT INTO bank_accounts (id, beneficiary, beneficiary_address, nickname, iban, bic, account_number, national_bank_code, national_branch_code, national_clearing_code, currency, country) VALUES ('33333333-3333-3333-3333-333333333333', 'Cornerstone FX Ltd', '10 Threadneedle Street, London', 'Primary EUR Settlement', 'GB82WEST123456987654321098765432', 'DEUTGB2LXXX', '98765432', '40', '123456', '12-34-56', 'EUR', 'GB');
INSERT INTO customer_accounts (id, name, description, account_type, account_state, account_manager, account_creation_time, silo) VALUES ('44444444-4444-4444-4444-444444444444', 'Cornerstone Principal Account', 'Operational customer account for settlement activity', 'CORPORATE', 'ACCEPTED', '55555555-5555-5555-5555-555555555555', TIMESTAMP '2024-03-01 09:30:00', 'treasury-core');
INSERT INTO corporation_audits (resource, version, name, code, incorporation_date, incorporation_country, type, duplicates) VALUES ('11111111-1111-1111-1111-111111111111', 1, 'Cornerstone FX Ltd', 'CFS-UK', '2017-01-15', 'GB', 'FINTECH', NULL);
INSERT INTO person_audits (resource, version, first_name, last_name, duplicates) VALUES ('22222222-2222-2222-2222-222222222222', 1, 'Stephen', 'Flynn', NULL);
INSERT INTO bank_account_audits (resource, version, beneficiary, beneficiary_address, nickname, iban, bic, account_number, national_bank_code, national_branch_code, national_clearing_code, currency, country) VALUES ('33333333-3333-3333-3333-333333333333', 1, 'Cornerstone FX Ltd', '10 Threadneedle Street, London', 'Primary EUR Settlement', 'GB82WEST123456987654321098765432', 'DEUTGB2LXXX', '98765432', '40', '123456', '12-34-56', 'EUR', 'GB');
INSERT INTO beneficial_owners (account_id, account_type, owner_id, owner_type) VALUES ('33333333-3333-3333-3333-333333333333', 'BANK_ACCOUNT', '22222222-2222-2222-2222-222222222222', 'PERSON');
INSERT INTO beneficial_owners (account_id, account_type, owner_id, owner_type) VALUES ('33333333-3333-3333-3333-333333333333', 'BANK_ACCOUNT', '11111111-1111-1111-1111-111111111111', 'CORPORATION');
INSERT INTO beneficial_owners (account_id, account_type, owner_id, owner_type) VALUES ('44444444-4444-4444-4444-444444444444', 'CUSTOMER_ACCOUNT', '22222222-2222-2222-2222-222222222222', 'PERSON');
INSERT INTO beneficial_owners (account_id, account_type, owner_id, owner_type) VALUES ('44444444-4444-4444-4444-444444444444', 'CUSTOMER_ACCOUNT', '11111111-1111-1111-1111-111111111111', 'CORPORATION');

