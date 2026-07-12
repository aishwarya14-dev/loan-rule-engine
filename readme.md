# Loan Rule Engine

## Overview

Loan Rule Engine is a configurable backend application built using **Java 21**, **Spring Boot**, **ANTLR4**, **PostgreSQL**, **Redis**, **Docker**, **Prometheus**, and **Grafana** for evaluating loan applications through business-defined rules.

Instead of hardcoding eligibility logic, business users can define loan approval rules using a Domain Specific Language (DSL). These rules are parsed into an Abstract Syntax Tree (AST) and evaluated against loan applications using the Composite and Strategy design patterns.

The engine supports multiple loan products, configurable factor importance, weighted scoring, hard rejection rules, rule caching, metrics collection, and extensible evaluation logic.



                                    +--------------------+
                                    |      Client        |
                                    |  REST / Swagger UI |
                                    +---------+----------+
                                              |
                                              |
                                              v
                             +-------------------------------+
                             | Spring Boot REST Controllers  |
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
                        | Rule Engine Service                        |
                        +----------------+---------------------------+
                                         |
                       +-----------------+-------------------+
                       |                                     |
                       v                                     v
          +----------------------------+       +-----------------------------+
          | Static Rule Evaluator      |       | Dynamic Rule Evaluator      |
          | JSON Rules                 |       | DSL Rules                   |
          +-------------+--------------+       +-------------+---------------+
                        |                                    |
                        |                                    |
                        v                                    v
             +-----------------------+          +-----------------------------+
             | Static Rule Factory   |          | DSL Rule Loader             |
             +-----------------------+          | Redis Cache                 |
                                                | PostgreSQL                 |
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
                                             | Abstract Syntax Tree (AST)   |
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
                                 | > >= < <= == !=                   |
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
                           | Group by Factor                                 |
                           | Apply Configurable Factor Importance            |
                           | Calculate Final Weighted Score                 |
                           | Detect Hard Reject                             |
                           | Generate Final Decision                        |
                           +----------------------+---------------------------+
                                                  |
                                                  |
                                                  v
                                    +-------------------------------+
                                    | PostgreSQL                    |
                                    | LoanApplicationResult         |
                                    | RuleResult                    |
                                    +-------------------------------+

               +------------------------------------------------------+
               | Monitoring                                            |
               | Micrometer → Prometheus → Grafana                    |
               +------------------------------------------------------+
---

# Features

* DSL-based business rule definition
* Dynamic rule parsing using ANTLR4
* Support for simple and composite (AND/OR) rules
* Dynamic rule storage in PostgreSQL
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
* Guarantor
* Compliance

Each loan type defines the business importance of every factor through the `loan_type_factor_config` table.

This allows different loan products to prioritize different evaluation criteria without modifying application code.

For example:

| Loan Type     | Credit Profile | Income Profile | Property |
| ------------- | -------------: | -------------: | -------: |
| Home Loan     |              4 |              5 |        5 |
| Personal Loan |              5 |              3 |        1 |

---

# Dynamic Weight Calculation

Unlike traditional rule engines where every rule has a fixed predefined weight, this engine derives scoring dynamically from configurable factor importance.

For every evaluated rule:

1. The rule's associated factor importance is retrieved from the `loan_type_factor_config` table.
2. The importance values of all evaluated rules are aggregated.
3. Each rule contributes proportionally according to the aggregate weight of its business factor.

This design allows business administrators to adjust the relative influence of different evaluation factors simply by updating configuration data rather than modifying application code.

The final score is calculated as the weighted sum of individual rule contributions.

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
