You define the rules of your custom DSL in an ANTLR grammar file, and ANTLR creates the parser to understand it.

# FinBank Loan Rule Engine

A domain-specific language (DSL) based rule parsing and evaluation engine for automated loan approval decisions. Business users define rules in plain English-like syntax, which are parsed at runtime using ANTLR4, modeled as an Abstract Syntax Tree using the Composite Pattern, and evaluated against loan applications using the Strategy Pattern.

---

## Table of Contents

- [Overview](#overview)
- [How It Works](#how-it-works)
- [DSL Syntax](#dsl-syntax)
- [Project Structure](#project-structure)
- [Design Patterns and Why](#design-patterns-and-why)
- [Rule Sources](#rule-sources)
- [Validation Pipeline](#validation-pipeline)
- [Duplicate and Idempotency Handling](#duplicate-and-idempotency-handling)
- [Monitoring](#monitoring)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Tech Stack](#tech-stack)

---

## Overview

FinBank Rule Engine allows business users to define loan approval rules using a human-readable DSL without touching any Java code. Rules are stored as plain strings in the database, fetched at evaluation time, parsed by ANTLR4 into an AST, and evaluated against a loan application to produce an `APPROVE`, `REJECT`, or `REVIEW` decision.

```
IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve
IF creditScore < 600 AND existingLoans > 3 THEN reject
IF employmentType == 'SALARIED' OR employmentTenure > 5 THEN review
```

---

## How It Works

```
Rule Creation (DSL path)
─────────────────────────────────────────────────────────────
Business user submits DSL string via API
        │
        ▼
Multi-layer Validation
├── Syntax validation      (ANTLR lexer and parser error listeners)
├── Semantic validation    (field names, operator compatibility, DB lookup values)
└── Duplicate check        (normalized rule already exists)
        │
        ▼
Saved as raw DSL string in DB


Rule Evaluation
─────────────────────────────────────────────────────────────
Loan application submitted
        │
        ▼
Fetch all DSL strings from DB
        │
        ▼
ANTLR4 parses each DSL string into AST (Expression tree)
        │
        ▼
RuleEvaluation (Strategy Pattern)
├── SimpleRuleEvaluation     single Condition leaf
└── CompositeRuleEvaluation  recursive AND/OR tree
        │
        ▼
RuleResult per rule (passed/failed + message)
        │
        ▼
Final decision  APPROVE / REJECT / REVIEW
```

---

## DSL Syntax

### Grammar

```
statement  :  IF expression THEN action
expression :  expression OR expression
           |  expression AND expression
           |  ( expression )
           |  condition
condition  :  FIELD operator VALUE
operator   :  > | >= | < | <= | == | !=
action     :  approve | reject | review
```

### Operator Precedence (NOT YET SUPPORTED)

AND has higher precedence than OR, matching standard boolean logic. Parentheses can be used for explicit grouping.

```
IF creditScore > 700 OR income >= 50000 AND existingLoans < 3 THEN approve

evaluates as:

IF creditScore > 700 OR (income >= 50000 AND existingLoans < 3) THEN approve
```

### Supported Fields

| Field | Type | Example |
|---|---|---|
| `creditScore` | Integer | `creditScore > 700` |
| `loanAmount` | Integer | `loanAmount <= 500000` |
| `monthlyIncome` | Integer | `monthlyIncome >= 50000` |
| `existingLoans` | Integer | `existingLoans < 3` |
| `employmentTenure` | Integer | `employmentTenure > 2` |
| `companyRating` | Integer | `companyRating >= 4` |
| `region` | String | `region == 'NORTH'` |
| `employmentType` | String | `employmentType == 'SALARIED'` |

### Value Types

| Type | Example |
|---|---|
| Integer | `700`, `50000` |
| Decimal | `4.5`, `2.0` |
| String | `'NORTH'`, `'SALARIED'` |

### Constraints

- String fields only support `==` and `!=`
- Numeric fields support all six operators
- String values must be wrapped in single quotes
- String values are validated against DB lookup tables at parse time

### Example Rules

```
IF creditScore > 750 THEN approve

IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve

IF employmentType == 'SALARIED' OR employmentTenure > 5 THEN review

IF creditScore < 600 AND existingLoans > 3 THEN reject

IF (creditScore > 700 AND monthlyIncome >= 50000) AND (employmentType == 'SALARIED' OR employmentTenure > 3) THEN approve
```

---

## Design Patterns

### Composite Pattern — Expression Tree

Represents arbitrarily nested AND/OR conditions as a recursive tree. Evaluating any depth of nesting is a simple recursive traversal with no special casing needed for depth or complexity.

```
AndExpression
├── Condition (creditScore > 700)           leaf
└── OrExpression                            branch
    ├── Condition (monthlyIncome >= 50000)  leaf
    └── Condition (existingLoans < 3)       leaf
```

Adding a new expression type such as `NotExpression` requires only a new class. No existing evaluators, validators, or the core rule model need to change.

### Visitor Pattern — Parse Tree to AST

Separates ANTLR parse tree traversal from domain object construction. `Rule`, `Condition`, `AndExpression`, and `OrExpression` have zero dependency on ANTLR types. The visitor is the only class that knows about ANTLR.

```
visitor.visitStatement()     →   Rule
visitor.visitAndExpression() →   AndExpression
visitor.visitCondition()     →   Condition
```

### Strategy Pattern — Rule Evaluation

Each evaluator is independently testable and interchangeable. Adding a new evaluation strategy does not touch existing code.

```
RuleEvaluation (interface)
├── SimpleRuleEvaluation     →   single Condition
└── CompositeRuleEvaluation  →   AND/OR tree of RuleEvaluation instances
```

### Open/Closed Principle

The system is open for extension and closed for modification throughout:

- New expression type — add a class, add a visitor override
- New field — register one entry in `FieldAccessorRegistry`
- New action — add one enum value
- Nothing existing changes in any of these cases

---

## Rule Sources

The engine loads rules from two sources and merges them at evaluation time.

### Source 1 — DSL Strings from DB (Primary)

Business users create rules via the API. Rules are validated, stored as raw DSL strings, fetched at evaluation time, and parsed by ANTLR into a `Rule` domain object.

```
POST /api/rules
{ "ruleText": "IF creditScore > 700 AND monthlyIncome >= 50000 THEN approve" }
        │
        ▼
Validation pipeline
        │
        ▼
Saved as raw string in rules table
        │
        ▼  (at evaluation time)
ANTLR4 parse → LoanRulesVisitor → Rule
```

### Source 2 — Static JSON File (Secondary)

An optional `static-rules.json` file for seeding baseline rules. Jackson deserializes into `RuleDto`, which is mapped to `Rule` via `RuleMapper`. All Jackson annotations live in the DTO layer only — the domain model stays completely clean.

```
static-rules.json
        │
        ▼
ObjectMapper → RuleDto   (Jackson annotations here only)
        │
        ▼
RuleMapper.toDomain() → Rule   (clean domain object)
```

Both sources produce the same `Rule` domain object. Everything downstream — evaluation, metrics, result generation — is unaware of which source a rule came from.

---

## Validation Pipeline

Every DSL string passes through three layers before being saved.

```
POST /api/rules
        │
        ▼
@NotBlank                       null or empty input rejected
        │
        ▼
DslSyntaxValidator              ANTLR lexer and parser error listeners
        │
        ▼
DslSemanticValidator            unknown fields, wrong operator types,
                                invalid lookup values checked against DB
        │
        ▼
DslDuplicateValidator           normalized rule already exists in DB
        │
        ▼
Save to DB ✅
```

## Duplicate and Idempotency Handling

Three layers protect against duplicate loan applications.

### Layer 1 — Idempotency Key

Handles network timeouts where a client retries a request that already succeeded. The client generates a UUID and sends it as a header. If the key is seen again, the original cached response is returned with no second DB write.

```
POST /api/loan-applications
Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000
```

### Layer 2 — Active Application Check

Business rule enforced in the service layer. An applicant cannot have two PENDING applications for the same loan type simultaneously.

```java
existsByApplicantIdAndLoanTypeIdAndStatus(applicantId, loanTypeId, PENDING)
```

### Layer 3 — DB Unique Constraint

Safety net for race conditions. Two simultaneous requests can both pass the application-level check before either saves. The DB constraint ensures only one succeeds and the other throws a `DataIntegrityViolationException`, handled globally.

```java
@UniqueConstraint(columnNames = {"applicant_id", "loan_type_id", "status"})
```

---

## Monitoring

### Setup

```bash
docker-compose up -d
```

| Service | URL | Credentials |
|---|---|---|
| Spring Boot Actuator | `http://localhost:8080/actuator/prometheus` | — |
| Prometheus | `http://localhost:9090` | — |
| Grafana | `http://localhost:3000` | admin / admin |

### Custom Metrics

| Metric | Description |
|---|---|
| `rule.evaluation.total` | Total number of rule evaluations |
| `rule.evaluation.passed` | Rules that passed |
| `rule.evaluation.failed` | Rules that failed |
| `rule.evaluation.duration` | Time taken to evaluate rules |
| `rule.created.total` | Rules created via DSL |


### Prerequisites

- Java 17 or above
- Maven 3.8 or above
- PostgreSQL
- Docker and Docker Compose for Prometheus and Grafana

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3 | Application framework |
| ANTLR4 | DSL grammar definition and AST parsing |
| Jackson | JSON deserialization for static rules only |
| Micrometer | Metrics abstraction layer |
| Prometheus | Metrics scraping and storage |
| Grafana | Metrics visualization |
| PostgreSQL | Rule and application persistence |
| Maven | Build tool |
| Docker Compose | Local monitoring stack |