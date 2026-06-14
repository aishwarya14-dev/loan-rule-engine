-- EMPLOYMENT TYPE
CREATE TABLE employment_type (
    id BIGSERIAL PRIMARY KEY,
    employment_type VARCHAR(50) NOT NULL
);

-- JOB TITLE
CREATE TABLE job_title (
    id BIGSERIAL PRIMARY KEY,
    job_title VARCHAR(100) NOT NULL
);

-- REGION
CREATE TABLE region (
    id BIGSERIAL PRIMARY KEY,
    region_name VARCHAR(50) NOT NULL
);

-- LOAN TYPE
CREATE TABLE loan_type (
    id BIGSERIAL PRIMARY KEY,
    loan_type VARCHAR(50) NOT NULL,
    description TEXT,
    interest_rate DOUBLE PRECISION,
    max_term_in_months INT,
    max_loan_amount NUMERIC(19,2)
);

-- USERS
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    mobile_number VARCHAR(15)
);

CREATE TABLE dsl_rule (
    id BIGSERIAL PRIMARY KEY,
    dsl_rule TEXT,
    loan_type_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_dsl_rule_loan_type
        FOREIGN KEY (loan_type_id)
        REFERENCES loan_type(id)
);


CREATE TABLE loan_application (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    loan_type_id BIGINT NOT NULL,
    job_title_id BIGINT,
    region_id BIGINT,
    employment_type_id BIGINT,

    applicant_name VARCHAR(255),
    applicant_email VARCHAR(255),
    applicant_contact VARCHAR(50),

    credit_score INT,
    status VARCHAR(50),
    remarks TEXT,

    application_date TIMESTAMP,
    approval_date TIMESTAMP,

    monthly_income NUMERIC(12,2),
    existing_loans INT,
    loan_amount NUMERIC(19,2),
    interest_rate DOUBLE PRECISION,
    loan_tenure_months INT,

    age INT,
    company_rating INT,
    employment_tenure INT,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_loan_type FOREIGN KEY (loan_type_id) REFERENCES loan_type(id),
    CONSTRAINT fk_job_title FOREIGN KEY (job_title_id) REFERENCES job_title(id),
    CONSTRAINT fk_region FOREIGN KEY (region_id) REFERENCES region(id),
    CONSTRAINT fk_employment_type FOREIGN KEY (employment_type_id) REFERENCES employment_type(id)
);

CREATE TABLE rule_result (
    id BIGSERIAL PRIMARY KEY,
    passed BOOLEAN,
    message TEXT,
    expected_value TEXT,
    description TEXT,
    loan_application_id BIGINT,

    CONSTRAINT fk_rule_result_application
    FOREIGN KEY (loan_application_id)
    REFERENCES loan_application(id)
);



