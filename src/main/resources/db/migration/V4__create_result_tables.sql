
CREATE UNIQUE INDEX uq_active_application_per_user_loantype
ON loan_application (user_id, loan_type_id)
WHERE status = 'PENDING';

CREATE TABLE rule_result (
    id BIGSERIAL PRIMARY KEY,
    passed BOOLEAN NOT NULL,
    message VARCHAR(1000),
    rule_evaluation_score DOUBLE PRECISION,
    description VARCHAR(2000),
    loan_application_id BIGINT NOT NULL,
    loan_type_factor_config_id BIGINT NOT NULL,

    CONSTRAINT fk_rule_result_loan_application
        FOREIGN KEY (loan_application_id)
        REFERENCES loan_application(id),

    CONSTRAINT fk_rule_result_loan_type_factor_config
        FOREIGN KEY (loan_type_factor_config_id)
        REFERENCES loan_type_factor_config(id)
);


CREATE TABLE loan_application_result (
    id BIGSERIAL PRIMARY KEY,

    loan_application_id BIGINT NOT NULL UNIQUE,

    decision VARCHAR(50) NOT NULL,

    final_score DOUBLE PRECISION,

    evaluated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loan_application_result_application
        FOREIGN KEY (loan_application_id)
        REFERENCES loan_application(id)
);


CREATE TABLE factor_evaluation_result (
    id BIGSERIAL PRIMARY KEY,

    loan_application_result_id BIGINT NOT NULL,

    factor_id BIGINT NOT NULL,

    factor_score DOUBLE PRECISION NOT NULL,

    max_factor_score DOUBLE PRECISION NOT NULL,

    evaluated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_factor_eval_result
        FOREIGN KEY (loan_application_result_id)
        REFERENCES loan_application_result(id),

    CONSTRAINT fk_factor_eval_factor
        FOREIGN KEY (factor_id)
        REFERENCES factor(id)
);