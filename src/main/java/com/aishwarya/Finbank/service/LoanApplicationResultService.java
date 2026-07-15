package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.enums.ApplicationStatus;
import com.aishwarya.Finbank.enums.Decision;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.LoanApplicationResultRepo;
import com.aishwarya.Finbank.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class LoanApplicationResultService {

    private final RuleEngineMetrics metrics;
    private final LoanRepository loanRepository;

    public LoanApplicationResult calculateAndSaveLoanApplicationResult(List<RuleResult> ruleResultList, LoanApplication loanApplication,boolean isDynamic){
        double finalScore;
        Map<Factor,Double> factorMap = new ConcurrentHashMap<Factor,Double>();
        if(!isDynamic)
            finalScore = calculateFinalScoreForStaticRuleSet(ruleResultList);
        else{
            // For dynamic ruleset, we need to calculate the final score based on the factor weights
            Integer totalWeight = buildDistributedFactorWeights(ruleResultList,factorMap);
            finalScore = calculateFinalScoreForDynamicRuleset(ruleResultList,totalWeight,factorMap);
        }
        return saveLoanApplicationResult(loanApplication,finalScore);
    }

    private double calculateFinalScoreForStaticRuleSet(List<RuleResult> ruleResultList){
        double finalScore = 0.0;
        for(RuleResult ruleResult : ruleResultList)
            finalScore += ruleResult.getRuleEvaluationScore();
        return finalScore;
    }

    private Integer buildDistributedFactorWeights(List<RuleResult> ruleResultList, Map<Factor,Double> factorMap){
        Integer totalWeight = 0;
        // number of rules present in each factor
        Map<Factor, Long> ruleCount =
                ruleResultList.stream()
                        .collect(Collectors.groupingBy(
                                r -> r.getLoanTypeFactorConfig().getFactor(),
                                Collectors.counting()
                        ));

        Set<Factor> processedFactors = new HashSet<>();
        for (RuleResult ruleResult : ruleResultList) {
            LoanTypeFactorConfig config = ruleResult.getLoanTypeFactorConfig();
            Factor factor = config.getFactor();

            if (processedFactors.add(factor)) {
                int factorWeight = config.getImportanceLevel().getWeight();
                totalWeight += factorWeight;

                // Distribute the factor weight equally among its rules
                double distributedWeight =
                        (double) factorWeight / ruleCount.get(factor);
                factorMap.put(factor, distributedWeight);
            }
        }
        double sum = factorMap.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        log.info("Sum of factor weights = {}", sum);
        log.info("Total Weight = {}", totalWeight);

        factorMap.forEach((factor, weight) ->
                log.info("{} -> {}", factor.getName(), weight));
        return totalWeight;
    }

    private double calculateFinalScoreForDynamicRuleset(List<RuleResult> ruleResultList, Integer totalWeight, Map<Factor,Double> factorMap){
        double finalScore = 0.0;
        for(RuleResult ruleResult : ruleResultList){
            if (ruleResult.isHardReject()){
                return 0.0; // If any rule result is a hard reject, the final score is 0
            }
            // Get the factor associated with the rule result
            Factor factor = ruleResult.getLoanTypeFactorConfig().getFactor();
            // Calculate the weighted score for the rule result based on its factor's weight
            log.info("normalized factor weight for factor {} with weight {}", factor.getName(),String.valueOf( factorMap.get(factor) / totalWeight));
            log.info("rule evaluation score is  {}",ruleResult.getRuleEvaluationScore());
            finalScore +=  ruleResult.getRuleEvaluationScore() * (factorMap.get(factor) / totalWeight);
            log.info("final score on adding weighted rule evaluation score {}",finalScore);
        }
        return finalScore;
    }

    private LoanApplicationResult saveLoanApplicationResult(LoanApplication loanApplication,double finalScore){
        // create the loan application result object
        LoanApplicationResult loanApplicationResult = new LoanApplicationResult();
        loanApplicationResult.setFinalScore(finalScore);
        Decision decision = getDecision(finalScore,loanApplication);
        loanApplicationResult.setDecision(decision);

        loanApplication.updateResult(loanApplicationResult);
        //save loan application to the db
        loanRepository.save(loanApplication);

        return loanApplicationResult;
    }

    private Decision getDecision(double finalScore,LoanApplication loanApplication){
        if(finalScore >= 0.75){
            loanApplication.updateApplicationStatus(ApplicationStatus.APPROVED);
            loanApplication.updateApprovalDate(LocalDateTime.now());
            metrics.incrementApproved();
            return Decision.APPROVE;
        } else if(finalScore < 0.40){
            loanApplication.updateApplicationStatus(ApplicationStatus.REJECTED);
            metrics.incrementRejected();
            return Decision.REJECT;
        }
        metrics.incrementReview();
        loanApplication.updateApplicationStatus(ApplicationStatus.UNDER_REVIEW);
        return Decision.REVIEW;
    }
}
