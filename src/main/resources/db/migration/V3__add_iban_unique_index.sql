-- =====================================================
-- V3: Add unique index on bank_accounts.iban for
-- faster lookups and duplicate prevention.
-- =====================================================

CREATE UNIQUE INDEX idx_bank_accounts_iban ON bank_accounts(iban);

