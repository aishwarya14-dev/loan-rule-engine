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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class LoanApplicationResultService {

    private final RuleEngineMetrics metrics;
    private final LoanRepository loanRepository;

    public LoanApplicationResult calculateAndSaveLoanApplicationResult(List<RuleResult> ruleResultList, LoanApplication loanApplication,boolean isDynamic){
        double finalScore = 0.0;
        Map<Factor,Integer> factorMap = new ConcurrentHashMap<Factor,Integer>();
        if(!isDynamic)
            finalScore = calculateFinalScoreForStaticRuleSet(ruleResultList);
        else{
            // For dynamic ruleset, we need to calculate the final score based on the factor weights
            Integer totalWeight = storeFactorMappingAndGetTotalWeight(ruleResultList,factorMap);
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

    private Integer storeFactorMappingAndGetTotalWeight(List<RuleResult> ruleResultList, Map<Factor,Integer> factorMap){
        Integer totalWeight = 0;
        for(RuleResult ruleResult: ruleResultList){
            // Get the loan type factor config associated with the rule result
            LoanTypeFactorConfig loanTypeFactorConfig = ruleResult.getLoanTypeFactorConfig();
            // Get the weight of the factor from the loan type factor config and add it to the total weight
            Integer factorWeight = loanTypeFactorConfig.getImportanceLevel().getWeight();
            totalWeight += factorWeight;
            Factor factor = loanTypeFactorConfig.getFactor();
            // Update the factor map with the new weight for the factor
            factorMap.put(factor, factorMap.getOrDefault(factor,0) + factorWeight);
        }
        return totalWeight;
    }

    private double calculateFinalScoreForDynamicRuleset(List<RuleResult> ruleResultList, Integer totalWeight, Map<Factor,Integer> factorMap){
        double finalScore = 0.0;
        for(RuleResult ruleResult : ruleResultList){
            if (ruleResult.isHardReject()){
                return 0.0; // If any rule result is a hard reject, the final score is 0
            }
            // Get the factor associated with the rule result
            Factor factor = ruleResult.getLoanTypeFactorConfig().getFactor();
            // Calculate the weighted score for the rule result based on its factor's weight
            log.info("factor weight for factor %s{} with weight %s{}", String.valueOf((double) factorMap.get(factor) / totalWeight), factor.getName());
            log.info("rule evaluation score is  %s{}",ruleResult.getRuleEvaluationScore());
            finalScore +=  ruleResult.getRuleEvaluationScore() * ((double) factorMap.get(factor) / totalWeight);
            log.info("final score on adding weighted rule evaluation score %s{}",finalScore);
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
