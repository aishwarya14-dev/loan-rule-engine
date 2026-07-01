INSERT INTO loan_type_factor_config
(loan_type_id, factor_id, importance_level_id)
VALUES
(1, 1, 1),   -- Home Loan - Credit Score - High
(1, 2, 2),   -- Home Loan - Annual Income - High
(1, 3, 2),   -- Home Loan - Employment Type - High
(1, 4, 5),   -- Home Loan - Existing EMI - Low
(1, 5, 3),   -- Home Loan - Credit Score - High
(1, 6, 3),   -- Home Loan - Annual Income - Medium
(1, 7, 3),   -- Home Loan - Employment Type - Medium
(1, 8, 4),   -- Home Loan - Existing EMI - Low
(1, 9, 4),   -- Home Loan - Existing EMI - Low

(2, 1, 1),   -- Personal Loan - Credit Score - High
(2, 2, 2),   -- Personal Loan - Annual Income - High
(2, 3, 3),   -- Personal Loan - Employment - Medium
(2, 4, 3),   -- Personal Loan - Debt Profile - Medium
(2, 5, 2),   -- Personal Loan - Property Value - High
(2, 6, 2),   -- Personal Loan - Loan To Value Ratio - High
(2, 7, 3),   -- Personal Loan - Region Check - Medium
(2, 8, 3),   -- Personal Loan - Financial Stability - Medium
(2, 9, 3),   -- Personal Loan - Employment Stability - Medium

(3, 1, 1),   -- Car Loan - Credit Score - High
(3, 2, 2),   -- Car Loan - Annual Income - Medium
(3, 3, 4),   -- Car Loan - Employment Type - Low
(3, 4, 3),   -- Car Loan - Age - Medium
(3, 5, 2),   -- Car Loan - Credit Score - High
(3, 6, 3),   -- Car Loan - Annual Income - Medium
(3, 7, 4),   -- Car Loan - Employment Type - Low
(3, 8, 3),   -- Car Loan - Age - Medium
(3, 9, 3);   -- Car Loan - Age - Medium