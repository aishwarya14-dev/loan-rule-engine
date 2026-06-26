# Loan Rule Engine

A configurable **Loan Underwriting Decision Engine** that enables business users to define loan approval policies using a custom Domain-Specific Language (DSL). Rules are authored in a human-readable format, validated, parsed at runtime using **ANTLR4**, transformed into an **Abstract Syntax Tree (AST)**, and evaluated against loan applications using the **Composite** and **Strategy** design patterns.

Unlike traditional rule engines that rely solely on rule priorities, this engine implements a **factor-based conflict resolution strategy**. Rules are grouped into business factors such as Credit Score, Income, Employment, and Debt, each with configurable importance. Matched rules contribute evidence towards factor scores, which are normalized to produce an explainable final decision while supporting severity-based overrides for non-negotiable business rules.

---

# Table of Contents

* Overview
* Key Features
* System Architecture
* Rule Authoring using DSL
* Rule Evaluation Pipeline
* Conflict Resolution & Decision Strategy
* Domain Model
* DSL Grammar
* Validation Pipeline
* Rule Sources
* Design Patterns
* Duplicate & Idempotency Handling
* Monitoring
* Getting Started
* API Reference
* Tech Stack
* Future Enhancements

---

# Overview

Loan Rule Engine provides a configurable platform for automating loan underwriting decisions.

Business users define eligibility policies using a custom DSL without modifying application code. Rules are validated, stored as DSL strings, parsed using ANTLR4 at runtime, and evaluated against incoming loan applications.

The engine separates **rule authoring**, **rule evaluation**, and **decision making**, enabling different loan products to have independent underwriting policies while reusing the same evaluation infrastructure.

Unlike traditional rule engines where conflicting APPROVE and REJECT rules compete based on priority, this engine evaluates business evidence using configurable business factors and produces transparent, explainable decisions.

---

# Key Features

* Custom DSL for business rule authoring
* Runtime parsing using ANTLR4
* Recursive AST evaluation using Composite Pattern
* Strategy-based evaluation engine
* Configurable Loan Types
* Configurable Business Factors
* Factor Importance based scoring
* Rule Strength based contribution
* Derived Factors for composite business logic
* Factor-based conflict resolution
* Severity-based rule overrides
* Multi-layer rule validation
* Duplicate rule detection
* Metrics using Micrometer, Prometheus and Grafana
* Clean domain model independent of parsing framework



---

# System Architecture

```
                     Rule Creation

Business User
      │
      ▼
Custom DSL
      │
      ▼
Validation Pipeline
      │
      ▼
Store DSL String
      │
      ▼
────────────────────────────────────────────

                 Loan Evaluation

Loan Application
      │
      ▼
Load Rules
      │
      ▼
ANTLR4 Parser
      │
      ▼
Abstract Syntax Tree
      │
      ▼
Rule Evaluation
      │
      ▼
Matched Rule Results
      │
      ▼
Business Factor Aggregation
      │
      ▼
Conflict Resolution
      │
      ▼
Normalized Score
      │
      ▼
Severity Overrides
      │
      ▼
APPROVE / REVIEW / REJECT
```

---

# Rule Authoring using DSL

Business users define rules in a human-readable syntax.

```
IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve

IF creditScore < 600 AND existingLoans > 3 THEN reject

IF employmentType == 'SALARIED' OR employmentTenure > 5 THEN review
```

The grammar is defined once using ANTLR.

ANTLR generates the lexer and parser automatically.

The application uses a Visitor implementation to convert the generated parse tree into domain objects independent of ANTLR.

---

# Rule Evaluation Pipeline

```
Loan Application
       │
       ▼
Fetch DSL Rules
       │
       ▼
ANTLR Parser
       │
       ▼
Expression Tree (AST)
       │
       ▼
RuleEvaluation Strategy
       │
       ▼
RuleResult
       │
       ▼
Business Factor Aggregation
       │
       ▼
Normalized Score
       │
       ▼
Final Decision
```

---

# Conflict Resolution & Decision Strategy

Traditional rule engines resolve conflicts using rule priorities. As the number of rules grows, priority management becomes difficult and decisions become harder to explain.

This project instead adopts a Factor-Based Scoring Model combined with Severity-Based Overrides**.

## Business Factors

Rules are grouped into configurable business factors.

Example

| Factor              | Importance |
| ------------------- | ---------- |
| Credit Score        | Critical   |
| Income              | High       |
| Employment          | Medium     |
| Debt                | Critical   |
| Property Value      | High       |
| Financial Stability | Critical   |

Each loan type can define its own factor configuration.

---

## Rule Strength

Each rule contributes evidence towards its factor ranging between 0.0 to 1.0.

```
VERY_STRONG = 1

STRONG = 0.75

MEDIUM = 0.50

WEAK = 0.25
```

Matched rules contribute only within their assigned factor.

If multiple rules match inside the same factor, only the strongest matched rule contributes towards the factor score.

This prevents duplicate scoring.

---

## Composite Rules

Composite rules spanning multiple business dimensions are modeled as derived business factors.

Example

```
creditScore > 700
AND
monthlyIncome >= 50000
AND
employmentType == 'SALARIED'
```

belongs to

```
FINANCIAL_STABILITY
```

rather than contributing independently to Credit Score, Income and Employment.

This avoids double counting and improves explainability.

---

## Rule Severity

Severity determines whether a rule participates in scoring or overrides the scoring process.

```
HARD_REJECT

NORMAL
```

Examples of Hard Reject rules include:

* Blacklisted customer
* Fraud detected
* Sanctioned customer
* Invalid documentation

If any Hard Reject rule matches, evaluation terminates immediately.

Review Required rules override automatic approval but still allow scoring to complete.

---

## Final Decision

After factor scores are aggregated, the overall score is normalized.

```
Score ≥ 0.80

APPROVE
```

```
Score 0.50–0.75

REVIEW
```

```
Score < 0.50

REJECT
```

Severity overrides are then applied before returning the final decision.

---

# Domain Model

```
LoanType
      │
      ▼
LoanTypeFactorConfig
      │
      ▼
Factor
      │
      ▼
Rule
```

## LoanType

Represents a configurable loan product.

Examples

* Personal Loan
* Home Loan
* Education Loan

---

## Factor

Represents a business dimension used for underwriting.

Examples

* Credit Score
* Income
* Employment
* Debt
* Property Value
* LTV Ratio
* Financial Stability

---

## LoanTypeFactorConfig

Associates a factor with a loan type and defines its business importance.

Different loan types may assign different importance levels to the same factor.

---

## Rule

Each rule belongs to one business factor.

Rules define:

* DSL Expression
* Action
* Rule Strength
* Rule Severity

---

# DSL Grammar

```
statement

    : IF expression THEN action

expression

    : expression OR expression
    | expression AND expression
    | '(' expression ')'
    | condition

condition

    : FIELD operator VALUE

operator

    : > | >= | < | <= | == | !=

action

    : approve
    | reject
    | review
```

---

# Validation Pipeline

Every rule passes through three validation stages.

```
Rule Submission
        │
        ▼
Syntax Validation
        │
        ▼
Semantic Validation
        │
        ▼
Duplicate Detection
        │
        ▼
Persist Rule
```

Validation includes

* Lexer errors
* Parser errors
* Unknown fields
* Invalid operators
* Invalid lookup values
* Duplicate normalized rules

---

# Rule Sources

The engine supports multiple rule sources.

## DSL Rules

Business-authored DSL rules stored in PostgreSQL as a primary source of rules loan application is evaluated against.

## Static JSON Rules

Optional baseline rules loaded using Jackson acting as a fallback.

Both sources produce the same domain model before evaluation.

---

# Design Patterns

## Composite Pattern

Represents arbitrarily nested AND/OR expressions as recursive trees.

---

## Visitor Pattern

Separates ANTLR parse tree traversal from domain model construction.

---

## Strategy Pattern

Provides interchangeable rule evaluation strategies.

---

## Registry Pattern

FieldAccessorRegistry decouples DSL field names from LoanApplication object access.

---

## Open/Closed Principle

The engine can be extended with new

* expression types
* fields
* actions
* factors

without modifying existing evaluation logic.

---

# Duplicate & Idempotency Handling

Three layers prevent duplicate processing.

1. Idempotency Key

Protects against client retries.

2. Active Application Validation

Prevents multiple pending applications for the same applicant and loan type.

3. Database Constraints

Guarantees consistency under concurrent requests.

---

# Monitoring

Application metrics are exported using Micrometer.

Supported dashboards include

* Rule evaluations
* Rule evaluation duration
* Rules created
* Passed vs failed evaluations

Prometheus collects metrics while Grafana visualizes them.

---

# Getting Started

Prerequisites

* Java 17
* Maven
* PostgreSQL
* Docker
* Docker Compose

Run

```bash
mvn clean install

docker-compose up -d

mvn spring-boot:run
```

---

# API Reference

Typical endpoints include

```
POST /rule-engine/loan/loan-applications

POST /rule-engine/

POST /rule-engine/rules/dsl

POST /rule-engine/user/register

POST /rule-engine/user/login
```

---

# Tech Stack

| Technology      | Purpose                         |
|-----------------| ------------------------------- |
| Java 21         | Core language                   |
| Spring Boot 3   | Backend framework               |
| Spring Data JPA | Persistence                     |
| PostgreSQL      | Database                        |
| ANTLR4          | DSL parsing                     |
| Jackson         | Static rule deserialization     |
| Micrometer      | Metrics                         |
| Prometheus      | Metrics collection              |
| Grafana         | Monitoring dashboards           |
| Redis           | Caching and idempotency support |
| Docker Compose  | Local infrastructure            |
| Maven           | Build tool                      |

---

# Future Enhancements

* Rule versioning
* Role based access control
* Rule execution audit trail


---

## Why This Project?

The objective of this project is to demonstrate how a production-grade decision engine can be built using clean architecture and extensible design principles. It combines parsing, validation, domain modeling, configurable business rules, conflict resolution, observability, and explainable decision making into a reusable underwriting platform. Rather than implementing a simple rule evaluator, the project focuses on solving real-world challenges such as rule maintainability, business configurability, conflict resolution, and scalability that are commonly encountered in enterprise lending systems.
