CREATE INDEX idx_dsl_rule_loan_type
ON dsl_rule(loan_type_id);



CREATE UNIQUE INDEX idx_job_title
ON job_title(job_title);

CREATE UNIQUE INDEX idx_region_name
ON region(region_name);

CREATE UNIQUE INDEX idx_loan_type_name
ON loan_type(loan_type);

CREATE UNIQUE INDEX idx_users_mobile
ON users(mobile_number);



CREATE INDEX idx_application_user
ON loan_application(user_id);

CREATE INDEX idx_application_loan_type
ON loan_application(loan_type_id);

CREATE INDEX idx_application_job_title
ON loan_application(job_title_id);

CREATE INDEX idx_application_region
ON loan_application(region_id);

CREATE INDEX idx_application_employment
ON loan_application(employment_type_id);

CREATE INDEX idx_application_status
ON loan_application(status);



CREATE INDEX idx_user_application_date
ON loan_application(user_id, application_date DESC);

CREATE INDEX idx_status_loan
ON loan_application(status, loan_type_id);

CREATE INDEX idx_application_date
ON loan_application(application_date);



CREATE INDEX idx_application_email
ON loan_application(applicant_email);