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