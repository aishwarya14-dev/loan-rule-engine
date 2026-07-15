# Loan Rule Engine

## Demo video : https://youtu.be/WXH0x2GU3ps
## ER Diagram : https://drive.google.com/file/d/1O52Zm6ex7iAo0v926kP1TeE_JT2CEPVZ/view?usp=sharing

## Overview

Loan Rule Engine is a configurable backend application built using **Java 21**, **Spring Boot**, **ANTLR4**, **PostgreSQL**, **Redis**, **Docker**, **Prometheus**, and **Grafana** for evaluating loan applications through business-defined rules.
Instead of hardcoding eligibility logic, business users can define loan approval rules using a Domain Specific Language (DSL). These rules are parsed into an Abstract Syntax Tree (AST) and evaluated against loan applications using the Composite and Strategy design patterns.
The engine supports multiple loan types, configurable factor importance, weighted scoring, hard rejection rules, rule caching, metrics collection, and extensible evaluation logic.



                                    +--------------------+
                                    |      Client        |
                                    |  REST / Swagger UI |
                                    +---------+----------+
                                              |
                                              |
                                              v
                             +-------------------------------+
                             |        REST Controllers       |
                             +---------------+---------------+
                                             |
                                             |
                                             v
                          +---------------------------------------+
                          | Loan Application Service              |
                          | - Validate Request                    |
                          | - Map DTO → Entity                    |
                          | - Mock Verification                   |
                          | - Persist Loan Application            |
                          +----------------+----------------------+
                                           |
                                           |
                                           v
                        +--------------------------------------------+
                        |         Rule Engine Service                |
                        +----------------+---------------------------+
                                         |
                       +-----------------+-------------------+
                       |                                     |
                       v                                     v
          +----------------------------+       +-----------------------------+
          | Static Rule Loader         |       |     Dynamic Rule Loaader    |
          | JSON Rules                 |       |     DSL Rules               |
          +-------------+--------------+       +-------------+---------------+
                        |                                    |
                        |                                    |
                        v                                    v
             +-----------------------+          +-----------------------------+
             | Static Rules Evaluator|          | Dynamic Rules Evaluator     |
             +-----------------------+          | Redis Cache                 |
                                                | Database                    |
                                                +-------------+--------------+
                                                              |
                                                              |
                                                              v
                                            +-------------------------------+
                                            | ANTLR4 DSL Parser             |
                                            | Lexer + Parser + Visitor      |
                                            +---------------+---------------+
                                                            |
                                                            v
                                             +------------------------------+
                                             | Rule Object                  |
                                             |                              |
                                             | Expression                   |
                                             | ├── Condition                |
                                             | ├── AndExpression            |
                                             | └── OrExpression             |
                                             +---------------+--------------+
                                                             |
                                                             |
                                                             v
                                   +----------------------------------------------+
                                   | Rule Evaluation Factory                      |
                                   +----------------+-----------------------------+
                                                    |
                           +------------------------+------------------------+
                           |                                                 |
                           v                                                 v
             +----------------------------+               +------------------------------+
             | Simple Rule Evaluation     |               | Composite Rule Evaluation    |
             | Single Condition           |               | Recursive AND / OR           |
             +-------------+--------------+               +--------------+---------------+
                           |                                             |
                           +-------------------+-------------------------+
                                               |
                                               v
                          +-----------------------------------------------+
                          | Loan Field Accessor Registry                  |
                          | Field → Getter Function Mapping               |
                          +----------------------+------------------------+
                                                 |
                                                 |
                                                 v
                                 +------------------------------------+
                                 | Comparison Evaluator               |
                                 | > >= < <= == !=                    |
                                 +----------------+-------------------+
                                                  |
                                                  |
                                                  v
                                    +-------------------------------+
                                    | RuleResult                    |
                                    | Passed / Failed               |
                                    | Score Contribution            |
                                    | LoanTypeFactorConfig          |
                                    +---------------+---------------+
                                                    |
                                                    |
                                                    v
                           +--------------------------------------------------+
                           | Loan Application Result Service                  |
                           |                                                  |
                           | Aggregate Rule Results                           |
                           | Group by Factor                                  |
                           | Apply Configurable Factor Importance             |
                           | Calculate Final Weighted Score                   |
                           | Detect Hard Reject                               |
                           | Generate Final Decision                          |
                           +----------------------+---------------------------+
                                                  |
                                                  |
                                                  v
                                    +-------------------------------+
                                    | LoanApplicationResult         |
                                    | RuleResult                    |
                                    +-------------------------------+

               +------------------------------------------------------+
               | Monitoring                                           |
               | Micrometer → Prometheus → Grafana                    |
               +------------------------------------------------------+
---

# Features

* DSL-based business rule definition
* Dynamic rule parsing using ANTLR4
* Support for simple and composite (AND/OR) rules
* DslRule storage in PostgreSQL
* Redis-backed rule caching
* Configurable factor importance per loan type
* Weighted loan scoring
* Hard reject rules
* Metrics using Micrometer + Prometheus
* Grafana dashboards
* Dockerized deployment on EC2 (AWS)
* JWT secured APIs
* OpenAPI / Swagger documentation

---

# Tech Stack

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Redis
* ANTLR4
* Docker & Docker Compose
* Prometheus
* Grafana
* Micrometer
* Flyway
* MapStruct
* Lombok
* JUnit 5
* Mockito

---

# Rule Definition

Business users create rules using a simple DSL.

Example:

```text
IF creditScore >= 750 THEN approve

IF creditScore >= 700 AND monthlyIncome >= 100000 THEN approve

IF blacklisted == true THEN reject

IF employmentTenure >= 2 OR annualIncome >= 1200000 THEN approve
```

Supported operators

* >
* > =
* <
* <=
* ==
* !=

Supported data types

* Integer
* Decimal
* Boolean
* String
* Date
* DateTime
* NOTE : (Date & DateTime support to be added)

Supported actions

* approve
* reject
* review

---

# Rule Processing Pipeline

1. Rule is stored in the database.
2. ANTLR parses the DSL.
3. Parser builds an Abstract Syntax Tree.
4. Dynamic rules are cached in Redis.
5. Incoming loan applications are evaluated against every rule.
6. Each rule produces a RuleResult.
7. Rule results are aggregated into a final application score.
8. Final recommendation is generated.

---

# Rule Evaluation

The engine supports two categories of rules.

## Simple Rules

Rules containing a single condition.

Example

```text
IF creditScore >= 750 THEN approve
```
----
## ANTLR Parsing Pipleline

DSL Rule
↓
Lexer
↓
Parser
↓
AST
↓
Visitor
↓
Rule Object

---

## Composite Rules

Rules containing AND / OR operators.

Example

```text
IF creditScore >= 700
AND monthlyIncome >= 100000
THEN approve
```

or

```text
IF annualIncome >= 1200000
OR employmentTenure >= 5
THEN approve
```

Composite expressions are recursively evaluated until every leaf condition has been evaluated.

---

# Configurable Factor-Based Scoring

Every rule belongs to exactly one business factor.

Example factors include

* Income Profile
* Employment Profile
* Credit Profile
* Property
* Banking Relationship
* Debt Profile
* Compliance

Each loan type defines the business importance of every factor through the `loan_type_factor_config` table.

This allows different loan types to prioritize different evaluation criteria without modifying application code.

For example:

| Loan Type     | Credit Profile | Income Profile | Property |
| ------------- | -------------: | -------------: | -------: |
| Home Loan     |              4 |              5 |        5 |
| Personal Loan |              5 |              3 |        1 |

---

# Factor-Based Weighted Scoring

Unlike traditional rule engines where every rule has a fixed predefined weight, this engine derives scoring dynamically from configurable factor importance.
For every evaluated rule:

1. The rule's associated business factor and its importance level are retrieved from the `loan_type_factor_config` table.
2. The total importance across all evaluated factors is calculated.
3. Each factor's normalized weight is computed as:

   `Factor Weight = Factor Importance / Total Factor Importance`

4. If multiple rules belong to the same factor, the factor's normalized weight is distributed equally among those rules.
   `Per Rule Share = Factor Share/Number of Rules`

5. Each rule contributes its weighted score to the final application score based on its evidence weight and evaluation result.

This design makes factor influence fully configurable. Business administrators can adjust the relative importance of factors such as **Credit Profile**, **Income Profile**, or **Property** by updating configuration data in the database, without modifying application code.
The final application score is computed as the weighted sum of all individual rule contributions.

---

# Hard Reject Rules

Rules marked with **HARD_REJECT** immediately reject an application.

Example

```text
IF blacklisted == true THEN reject
```

When a Hard Reject rule evaluates to true:

* remaining score becomes zero
* application is immediately rejected

---

# Design Patterns

## Composite Pattern

Used to represent nested rule expressions.

```
Expression
│
├── Condition
├── AndExpression
└── OrExpression
```

---

## Strategy Pattern

Different evaluation strategies are used for:

* Simple rules
* Composite rules

---

## Factory Pattern

Factories create appropriate evaluation objects depending on rule type.

---

## Registry Pattern

Loan application fields are mapped dynamically using a registry instead of reflection.

---

# Architecture

```
Client
   │
   ▼
REST Controller
   │
   ▼
Loan Service
   │
   ▼
Rule Engine
   │
   ├── Static Rule Evaluator
   └── Dynamic Rule Evaluator
           │
           ▼
Composite Evaluation
           │
           ▼
Rule Results
           │
           ▼
Loan Application Result
```

---

# Monitoring

Application metrics are exported using Micrometer.

Collected metrics include

* Total rules evaluated
* Passed evaluations
* Failed evaluations
* DSL parsing duration
* Rule evaluation duration
* HTTP metrics
* JVM metrics

Metrics are scraped by Prometheus and visualized through Grafana dashboards.

---

# Security

* JWT Authentication
* Stateless session management
* Protected APIs
* Role-based access support

---

# Testing

The project contains

* Unit Tests
* Mockito-based service tests
* Rule evaluation tests
* Parser tests

---

# Future Enhancements

* Rule versioning
* Rule audit history
* Rule simulation mode
* Explainable decision reports
* Rule conflict detection
* Business rule management UI
* Event-driven rule evaluation
* Machine Learning assisted score calibration

---

# Author

**Aishwarya Mehrotra**

Backend Engineer | Java | Spring Boot | Rule Engine | System Design
