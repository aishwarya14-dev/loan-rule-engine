ALTER TABLE loan_application
DROP CONSTRAINT IF EXISTS fk_loan_application_result;

ALTER TABLE loan_application
DROP COLUMN IF EXISTS result_id;


ALTER TABLE loan_application_result
ADD CONSTRAINT uk_loan_application_result_application
UNIQUE (loan_application_id);

ALTER TABLE loan_application_result
DROP CONSTRAINT IF EXISTS fk_loan_application_result_loan_application;

ALTER TABLE loan_application_result
ADD CONSTRAINT fk_loan_application_result_loan_application
FOREIGN KEY (loan_application_id)
REFERENCES loan_application(id)
ON DELETE CASCADE;