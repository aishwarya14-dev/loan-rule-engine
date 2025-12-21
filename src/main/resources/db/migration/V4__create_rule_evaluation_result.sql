CREATE TABLE rule_evaluation_result (
    id BIGSERIAL PRIMARY KEY,
    rule_results JSONB,
    loan_application_id BIGINT NOT NULL,
    evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_loan FOREIGN KEY (loan_application_id) REFERENCES loan_application(id)
);
