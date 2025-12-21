CREATE TABLE IF NOT EXISTS loan_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    interest_rate DECIMAL(5,2),
    max_tenure_months INT NOT NULL,
);
