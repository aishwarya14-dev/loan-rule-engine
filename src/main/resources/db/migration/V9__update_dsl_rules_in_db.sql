INSERT INTO dsl_rule
(dsl_rule, loan_type_id, factor_id, evidence_weight, severity)
VALUES


-- HOME LOAN (loan_type_id = 1)

('IF creditScore >= 750 THEN approve',
1, 6, 0.95, 'NORMAL'),

('IF monthlyIncome >= 100000 THEN approve',
1, 1, 0.80, 'NORMAL'),

('IF debtToIncomeRatio > 50 THEN reject',
1, 3, 0.95, 'HARD_REJECT'),

('IF propertyValue >= 8000000 THEN approve',
1, 4, 0.75, 'NORMAL'),

('IF creditScore >= 760 AND monthlyIncome >= 120000 THEN approve',
1, 6, 0.98, 'NORMAL'),

('IF propertyVerified == false THEN reject',
1, 12, 1.00, 'HARD_REJECT'),

('IF loanToValueRatio <= 75 AND propertyValue >= 10000000 THEN approve',
1, 4, 0.90, 'NORMAL'),

('IF blacklisted == true THEN reject',
1, 12, 1.00, 'HARD_REJECT'),



-- PERSONAL LOAN (loan_type_id = 2)

('IF employmentTenure >= 24 THEN approve',
2, 2, 0.70, 'NORMAL'),

('IF monthlyIncome >= 50000 AND creditScore >= 700 THEN approve',
2, 1, 0.90, 'NORMAL'),

('IF bankruptcies > 0 THEN reject',
2, 6, 1.00, 'HARD_REJECT'),

('IF loanDefaults > 1 THEN reject',
2, 3, 1.00, 'HARD_REJECT'),

('IF salaryAccountWithBank == true THEN approve',
2, 10, 0.55, 'NORMAL'),



-- CAR LOAN (loan_type_id = 3)

('IF downPayment >= 500000 THEN approve',
3, 4, 0.75, 'NORMAL'),

('IF loanToValueRatio <= 80 THEN approve',
3, 4, 0.82, 'NORMAL'),

('IF existingCustomer == true AND averageAccountBalance >= 150000 THEN approve',
3, 10, 0.88, 'NORMAL'),

('IF debtToIncomeRatio >= 60 THEN reject',
3, 3, 0.95, 'HARD_REJECT'),

('IF fraudFlag == true THEN reject',
3, 12, 1.00, 'HARD_REJECT'),



-- EDUCATION LOAN (loan_type_id = 4)

('IF guarantorPresent == true THEN approve',
4, 8, 0.75, 'NORMAL'),

('IF guarantorPresent == false AND loanAmount > 2000000 THEN reject',
4, 8, 0.98, 'HARD_REJECT'),

('IF guarantorPresent == true AND monthlyIncome >= 40000 THEN approve',
4, 9, 0.80, 'NORMAL'),

('IF incomeVerified == false THEN reject',
4, 12, 1.00, 'HARD_REJECT'),

('IF kycVerified == true AND panVerified == true AND aadhaarVerified == true THEN approve',
4, 12, 0.95, 'NORMAL'),



-- BUSINESS LOAN (loan_type_id = 5)

('IF annualIncome >= 2500000 THEN approve',
5, 1, 0.85, 'NORMAL'),

('IF companyRating >= 4 AND employmentTenure >= 36 THEN approve',
5, 2, 0.90, 'NORMAL'),

('IF creditScore >= 780 AND debtToIncomeRatio < 35 AND annualIncome >= 3000000 THEN approve',
5, 6, 0.99, 'NORMAL'),

('IF region == "CENTRAL" THEN approve',
5, 5, 0.3, 'NORMAL'),

('IF aadhaarVerified == false OR panVerified == false OR kycVerified == false THEN reject',
5, 12, 1.00, 'HARD_REJECT');