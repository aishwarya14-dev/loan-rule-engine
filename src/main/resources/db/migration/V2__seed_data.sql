

-- EMPLOYMENT TYPES
INSERT INTO employment_type (employment_type) VALUES
('SALARIED'),
('SELF_EMPLOYED'),
('BUSINESS_OWNER'),
('FREELANCER');

-- JOB TITLES
INSERT INTO job_title (job_title) VALUES
('ENGINEER'),
('SENIOR_ENGINEER'),
('MANAGER'),
('SENIOR_MANAGER'),
('DIRECTOR'),
('ANALYST'),
('FRESHER');

-- REGIONS
INSERT INTO region (region_name) VALUES
('NORTH'),
('SOUTH'),
('EAST'),
('WEST'),
('CENTRAL');

-- LOAN TYPES
INSERT INTO loan_type (
    loan_type,
    description,
    interest_rate,
    max_term_in_months,
    max_loan_amount
) VALUES
('HOME_LOAN', 'Loan for purchasing property', 8.5, 360, 10000000),
('PERSONAL_LOAN', 'Unsecured personal loan', 12.0, 60, 1000000),
('CAR_LOAN', 'Loan for vehicle purchase', 9.0, 84, 1500000),
('EDUCATION_LOAN', 'Loan for higher education', 10.5, 120, 2000000),
('BUSINESS_LOAN', 'Loan for business expansion', 11.0, 180, 5000000);

-- USERS (sample data)
INSERT INTO users (username, password, role, mobile_number) VALUES
('admin', '$2a$10$hashedpassword1', 'ADMIN', '9999999999'),
('user1', '$2a$10$hashedpassword2', 'USER', '9876543210'),
('user2', '$2a$10$hashedpassword3', 'USER', '9123456780');