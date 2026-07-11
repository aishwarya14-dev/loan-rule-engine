package com.aishwarya.Finbank.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class RuleEngineMetrics {

    // Rule Evaluation
    private final Counter ruleEvaluationTotal;
    private final Counter ruleEvaluationPassed;
    private final Counter ruleEvaluationFailed;
    private final Counter ruleEvaluationSkipped;   // rules that threw exceptions
    private final Timer   ruleEvaluationDuration;

    // Rule Management
    private final Counter ruleCreatedTotal;
    private final Counter ruleSyntaxValidationFailed;
    private final Counter ruleSemanticValidationFailed;
    private final Counter ruleDuplicateRejected;

    // Loan Application
    private final Counter loanApplicationApproved;
    private final Counter loanApplicationRejected;
    private final Counter loanApplicationReview;
    private final Counter loanApplicationDuplicate;
    private final Timer   loanApplicationEvaluationDuration;

    // Cache
    private final Counter cacheEvictions;

    // DSL Parsing
    private final Counter dslParseSuccess;
    private final Counter dslParseFailed;
    private final Timer   dslParseDuration;

    public RuleEngineMetrics(MeterRegistry meterRegistry) {
        log.info("RuleEngineMetrics initialized");
        this.ruleEvaluationTotal = Counter.builder("rule.evaluation")
                .description("Total rule evaluations")
                .register(meterRegistry);
        this.ruleEvaluationPassed = Counter.builder("rule.evaluation.passed")
                .description("Rules that passed")
                .register(meterRegistry);

        this.ruleEvaluationFailed = Counter.builder("rule.evaluation.failed")
                .description("Rules that failed")
                .register(meterRegistry);

        this.ruleEvaluationSkipped = Counter.builder("rule.evaluation.skipped")
                .description("Rules skipped due to evaluation error")
                .register(meterRegistry);

        this.ruleEvaluationDuration = Timer.builder("rule.evaluation.duration")
                .description("Time to evaluate all rules for one application")
                .register(meterRegistry);

        // Rule management
        this.ruleCreatedTotal = Counter.builder("rule.created")
                .description("Total DSL rules created")
                .register(meterRegistry);

        this.ruleSyntaxValidationFailed = Counter.builder("rule.validation.syntax.failed")
                .description("DSL rules rejected due to syntax errors")
                .register(meterRegistry);

        this.ruleSemanticValidationFailed = Counter.builder("rule.validation.semantic.failed")
                .description("DSL rules rejected due to semantic errors")
                .register(meterRegistry);

        this.ruleDuplicateRejected = Counter.builder("rule.validation.duplicate.rejected")
                .description("DSL rules rejected as duplicates")
                .register(meterRegistry);

        // Loan application outcomes
        this.loanApplicationApproved = Counter.builder("loan.application.approved")
                .description("Loan applications approved")
                .register(meterRegistry);

        this.loanApplicationRejected = Counter.builder("loan.application.rejected")
                .description("Loan applications rejected")
                .register(meterRegistry);

        this.loanApplicationReview = Counter.builder("loan.application.review")
                .description("Loan applications sent for review")
                .register(meterRegistry);

        this.loanApplicationDuplicate = Counter.builder("loan.application.duplicate")
                .description("Duplicate loan applications blocked")
                .register(meterRegistry);

        this.loanApplicationEvaluationDuration = Timer.builder("loan.application.evaluation.duration")
                .description("End to end evaluation time per loan application")
                .register(meterRegistry);

        // Cache
        this.cacheEvictions = Counter.builder("rule.cache.evictions")
                .description("Number of rule cache evictions")
                .register(meterRegistry);

        // DSL parsing
        this.dslParseSuccess = Counter.builder("dsl.parse.success")
                .description("Successful DSL parses")
                .register(meterRegistry);

        this.dslParseFailed = Counter.builder("dsl.parse.failed")
                .description("Failed DSL parses")
                .register(meterRegistry);

        this.dslParseDuration = Timer.builder("dsl.parse.duration")
                .description("Time to parse a DSL rule string")
                .register(meterRegistry);
    }

    public void incrementEvaluationTotal()        { ruleEvaluationTotal.increment(); }
    public void incrementEvaluationPassed()       { ruleEvaluationPassed.increment(); }
    public void incrementEvaluationFailed()       { ruleEvaluationFailed.increment(); }
    public void incrementEvaluationSkipped()      { ruleEvaluationSkipped.increment(); }
    public void incrementRuleCreated()            { ruleCreatedTotal.increment(); }
    public void incrementSyntaxFailed()           { ruleSyntaxValidationFailed.increment(); }
    public void incrementSemanticFailed()         { ruleSemanticValidationFailed.increment(); }
    public void incrementDuplicateRejected()      { ruleDuplicateRejected.increment(); }
    public void incrementApproved()               { loanApplicationApproved.increment(); }
    public void incrementRejected()               { loanApplicationRejected.increment(); }
    public void incrementReview()                 { loanApplicationReview.increment(); }
    public void incrementDuplicateApplication()   { loanApplicationDuplicate.increment(); }
    public void incrementCacheEviction()          { cacheEvictions.increment(); }
    public void incrementDslParseSuccess()        { dslParseSuccess.increment(); }
    public void incrementDslParseFailed()         { dslParseFailed.increment(); }


    public <T> T recordEvaluationDuration(Supplier<T> task) {
        return ruleEvaluationDuration.record(task);
    }

    public <T> T recordApplicationEvaluationDuration(Supplier<T> task) {
        return loanApplicationEvaluationDuration.record(task);
    }

    public <T> T recordDslParseDuration(Supplier<T> task) {
        return dslParseDuration.record(task);
    }
}