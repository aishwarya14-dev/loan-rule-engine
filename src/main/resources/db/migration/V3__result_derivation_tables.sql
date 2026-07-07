CREATE TABLE factor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO factor(name)
VALUES
('Income Profile'),
('Employment Profile'),
('Debt Profile'),
('Property'),
('Region Check'),
('Credit Profile'),
('Loan Purpose'),
('Guarantor'),
('Co-Applicant'),
('Banking Relationship'),
('Residence'),
('Compliance')
;


CREATE TABLE importance_level (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    weight INTEGER NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO importance_level(name, weight)
VALUES
('Critical', 5),
('High', 4),
('Moderate', 3),
('Low', 2),
('Negligible', 1);


ALTER TABLE dsl_rule
    ADD COLUMN factor_id BIGINT REFERENCES factor(id),
    ADD COLUMN evidence_weight    DOUBLE PRECISION,
    ADD COLUMN severity    VARCHAR(50) NOT NULL DEFAULT 'NORMAL';


CREATE TABLE loan_type_factor_config (
    id                  BIGSERIAL PRIMARY KEY,
    loan_type_id        BIGINT NOT NULL REFERENCES loan_type(id),
    factor_id         BIGINT NOT NULL REFERENCES factor(id),
    importance_level_id BIGINT NOT NULL REFERENCES importance_level(id),
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    CONSTRAINT uq_loan_type_factor UNIQUE (loan_type_id, factor_id)
);
