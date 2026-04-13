-- V2 - Insert reference data (countries, currencies)

-- Insert sample countries
INSERT INTO countries (code, name, numeric_code, alpha3_code, is_eurozone, is_sepa) VALUES
('US', 'United States', '840', 'USA', FALSE, FALSE),
('GB', 'United Kingdom', '826', 'GBR', FALSE, TRUE),
('DE', 'Germany', '276', 'DEU', TRUE, TRUE),
('FR', 'France', '250', 'FRA', TRUE, TRUE),
('IT', 'Italy', '380', 'ITA', TRUE, TRUE),
('ES', 'Spain', '724', 'ESP', TRUE, TRUE),
('NL', 'Netherlands', '528', 'NLD', TRUE, TRUE),
('BE', 'Belgium', '056', 'BEL', TRUE, TRUE),
('CH', 'Switzerland', '756', 'CHE', FALSE, TRUE),
('SE', 'Sweden', '752', 'SWE', FALSE, TRUE),
('IE', 'Ireland', '372', 'IRL', TRUE, TRUE),
('AT', 'Austria', '040', 'AUT', TRUE, TRUE),
('GR', 'Greece', '300', 'GRC', TRUE, TRUE),
('PT', 'Portugal', '620', 'PRT', TRUE, TRUE),
('CZ', 'Czech Republic', '203', 'CZE', FALSE, TRUE),
('PL', 'Poland', '616', 'POL', FALSE, TRUE),
('CA', 'Canada', '124', 'CAN', FALSE, FALSE),
('AU', 'Australia', '036', 'AUS', FALSE, FALSE),
('JP', 'Japan', '392', 'JPN', FALSE, FALSE),
('SG', 'Singapore', '702', 'SGP', FALSE, FALSE);

-- Insert sample currencies
INSERT INTO currencies (code, name) VALUES
('USD', 'United States Dollar'),
('EUR', 'Euro'),
('GBP', 'British Pound'),
('JPY', 'Japanese Yen'),
('CAD', 'Canadian Dollar'),
('AUD', 'Australian Dollar'),
('CHF', 'Swiss Franc'),
('SEK', 'Swedish Krona'),
('SGD', 'Singapore Dollar'),
('HKD', 'Hong Kong Dollar'),
('CNY', 'Chinese Yuan'),
('INR', 'Indian Rupee'),
('MXN', 'Mexican Peso'),
('ZAR', 'South African Rand'),
('BRL', 'Brazilian Real');
