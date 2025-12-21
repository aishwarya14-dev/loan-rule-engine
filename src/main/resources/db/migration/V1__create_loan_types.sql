CREATE TABLE IF NOT EXISTS loan_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE ,
    description TEXT,
    interest_rate DECIMAL(5,2) NOT NULL,
    max_tenure_months INT NOT NULL
);

INSERT INTO loan_types (name, description, interest_rate, max_tenure_months) VALUES
('Personal Loan', 'Unsecured loan for personal use', 12.5, 60),
('Home Loan', 'Loan for home purchase or construction', 8.3, 240),
('Auto Loan', 'Loan to purchase a vehicle', 9.1, 84),
('Education Loan', 'Loan for educational expenses', 10.0, 120),
('Business Loan', 'Loan for business purposes', 11.0, 180),
('Gold Loan', 'Secured loan against gold assets', 7.5, 36),
('Agricultural Loan', 'Loan for agricultural activities', 6.0, 120),
('Medical Loan', 'Loan for medical expenses', 10.5, 60),
('Travel Loan', 'Loan for travel expenses', 13.0, 24),
('Wedding Loan', 'Loan for wedding expenses', 14.0, 36)
ON CONFLICT (name) DO NOTHING;
