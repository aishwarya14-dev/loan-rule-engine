package com.aishwarya.FinBank.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class RuleMetrics {
    private final Counter ruleEvaluationTotal;
    private final Counter ruleEvaluationPassed;
    private final Counter ruleEvaluationFailed;
    private final Counter ruleCreatedTotal;
    private final Timer ruleEvaluationDuration;

    public RuleMetrics(MeterRegistry meterRegistry) {
        this.ruleEvaluationTotal = Counter.builder("rule.evaluation.total")
                .description("Total number of rule evaluations")
                .register(meterRegistry);

        this.ruleEvaluationPassed = Counter.builder("rule.evaluation.passed")
                .description("Total rules that passed")
                .register(meterRegistry);

        this.ruleEvaluationFailed = Counter.builder("rule.evaluation.failed")
                .description("Total rules that failed")
                .register(meterRegistry);

        this.ruleCreatedTotal = Counter.builder("rule.created.total")
                .description("Total rules created via DSL")
                .register(meterRegistry);

        this.ruleEvaluationDuration = Timer.builder("rule.evaluation.duration")
                .description("Time taken to evaluate rules")
                .register(meterRegistry);
    }


    public void incrementTotal() {
        ruleEvaluationTotal.increment();
    }

    public void incrementPassed() {
        ruleEvaluationPassed.increment();
    }

    public void incrementFailed() {
        ruleEvaluationFailed.increment();
    }

    public void incrementRuleCreated() {
        ruleCreatedTotal.increment();
    }

    public void recordEvaluationTime(Runnable task) {
        ruleEvaluationDuration.record(task);
    }
}
