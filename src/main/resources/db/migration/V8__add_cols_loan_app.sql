CREATE TABLE property_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO property_type (name) VALUES
('RESIDENTIAL'),
('APARTMENT'),
('VILLA'),
('PLOT'),
('COMMERCIAL'),
('INDUSTRIAL'),
('AGRICULTURAL_LAND');

CREATE TABLE industry (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO industry (name) VALUES
('INFORMATION_TECHNOLOGY'),
('BANKING_AND_FINANCE'),
('GOVERNMENT'),
('HEALTHCARE'),
('EDUCATION'),
('MANUFACTURING'),
('AUTOMOBILE'),
('TELECOMMUNICATION'),
('RETAIL'),
('HOSPITALITY'),
('CONSTRUCTION'),
('REAL_ESTATE'),
('TRANSPORTATION'),
('LOGISTICS'),
('AGRICULTURE'),
('FREELANCE'),
('STARTUP');

CREATE TABLE loan_purpose (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO loan_purpose (name) VALUES
('HOME_PURCHASE'),
('HOME_CONSTRUCTION'),
('HOME_RENOVATION'),
('VEHICLE_PURCHASE'),
('EDUCATION'),
('BUSINESS_EXPANSION'),
('WORKING_CAPITAL'),
('MEDICAL_EMERGENCY'),
('PERSONAL_EXPENSE'),
('DEBT_CONSOLIDATION'),
('VACATION'),
('WEDDING');



ALTER TABLE loan_application

-- New Foreign Keys
ADD COLUMN property_type_id BIGINT,
ADD COLUMN industry_id BIGINT,
ADD COLUMN loan_purpose_id BIGINT,
ADD COLUMN result_id BIGINT,

-- Credit Profile
ADD COLUMN credit_history_years INT,
ADD COLUMN total_outstanding_debt NUMERIC(15,2),
ADD COLUMN credit_card_utilization NUMERIC(5,2),
ADD COLUMN missed_payments_last_12_months INT DEFAULT 0,
ADD COLUMN bankruptcies INT DEFAULT 0,

-- Income Profile
ADD COLUMN annual_income NUMERIC(15,2),
ADD COLUMN other_monthly_income NUMERIC(15,2),
ADD COLUMN income_verified BOOLEAN DEFAULT FALSE,
ADD COLUMN income_tax_return_available BOOLEAN DEFAULT FALSE,

-- Employment Profile
ADD COLUMN employer_name VARCHAR(255),
ADD COLUMN probation_completed BOOLEAN DEFAULT TRUE,
ADD COLUMN salary_account_with_bank BOOLEAN DEFAULT FALSE,

-- Debt Profile
ADD COLUMN monthly_emi NUMERIC(15,2),
ADD COLUMN debt_to_income_ratio NUMERIC(5,2),
ADD COLUMN loan_defaults INT DEFAULT 0,
ADD COLUMN guarantor_present BOOLEAN DEFAULT FALSE,

-- Property Details
ADD COLUMN property_value NUMERIC(15,2),
ADD COLUMN property_age INT,
ADD COLUMN property_verified BOOLEAN DEFAULT FALSE,

-- Loan Details
ADD COLUMN down_payment NUMERIC(15,2),
ADD COLUMN loan_to_value_ratio NUMERIC(5,2),

-- Banking Relationship
ADD COLUMN existing_customer BOOLEAN DEFAULT FALSE,
ADD COLUMN customer_since DATE,
ADD COLUMN average_account_balance NUMERIC(15,2),
ADD COLUMN has_fixed_deposit BOOLEAN DEFAULT FALSE,

-- Residence
ADD COLUMN residence_years INT,
ADD COLUMN owns_house BOOLEAN DEFAULT FALSE,

-- Compliance / Risk
ADD COLUMN kyc_verified BOOLEAN DEFAULT FALSE,
ADD COLUMN pan_verified BOOLEAN DEFAULT FALSE,
ADD COLUMN aadhaar_verified BOOLEAN DEFAULT FALSE,
ADD COLUMN fraud_flag BOOLEAN DEFAULT FALSE,
ADD COLUMN blacklisted BOOLEAN DEFAULT FALSE,

-- Audit Columns
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


ALTER TABLE loan_application

ADD CONSTRAINT fk_loan_application_property_type
FOREIGN KEY (property_type_id)
REFERENCES property_type(id),

ADD CONSTRAINT fk_loan_application_industry
FOREIGN KEY (industry_id)
REFERENCES industry(id),

ADD CONSTRAINT fk_loan_application_loan_purpose
FOREIGN KEY (loan_purpose_id)
REFERENCES loan_purpose(id),

ADD CONSTRAINT fk_loan_application_result
FOREIGN KEY (result_id)
REFERENCES loan_application_result(id);


CREATE TABLE co_applicant (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL,

    name VARCHAR(255),
    email VARCHAR(255),
    contact VARCHAR(20),

    age INT,
    monthly_income NUMERIC(15,2),
    annual_income NUMERIC(15,2),
    credit_score INT,
    employment_type_id BIGINT,
    employment_tenure INT,
    company_rating INT,
    existing_loans INT,

    FOREIGN KEY (loan_application_id)
        REFERENCES loan_application(id),

    FOREIGN KEY (employment_type_id)
        REFERENCES employment_type(id)
);


CREATE TABLE guarantor (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL,

    name VARCHAR(255),
    contact VARCHAR(20),
    age INT,
    monthly_income NUMERIC(15,2),
    annual_income NUMERIC(15,2),
    credit_score INT,
    net_worth NUMERIC(15,2),
    existing_loans INT,
    relationship VARCHAR(100),
    employment_type_id BIGINT,

    FOREIGN KEY (loan_application_id)
        REFERENCES loan_application(id),

    FOREIGN KEY (employment_type_id)
        REFERENCES employment_type(id)
);