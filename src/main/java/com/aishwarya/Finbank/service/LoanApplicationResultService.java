package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.enums.Decision;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.LoanApplicationResultRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class LoanApplicationResultService {

    private final LoanApplicationResultRepo loanApplicationResultRepo;
    private final RuleEngineMetrics metrics;

    public void calculateAndSaveLoanApplicationResult(List<RuleResult> ruleResultList, LoanApplication loanApplication,boolean isDynamic){
        double finalScore = 0.0;
        Map<Factor,Integer> factorMap = new ConcurrentHashMap<Factor,Integer>();
        if(!isDynamic)
            finalScore = calculateFinalScoreForStaticRuleSet(ruleResultList);
        else{
            // For dynamic ruleset, we need to calculate the final score based on the factor weights
            Integer totalWeight = storeFactorMappingAndGetTotalWeight(ruleResultList,factorMap);
            finalScore = calculateFinalScoreForDynamicRuleset(ruleResultList,totalWeight,factorMap);
        }
        saveLoanApplicationResult(loanApplication,finalScore);
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
            finalScore +=  ruleResult.getRuleEvaluationScore() * ((double) factorMap.get(factor) / totalWeight);
        }
        return finalScore;
    }

    private void saveLoanApplicationResult(LoanApplication loanApplication,double finalScore){
        LoanApplicationResult loanApplicationResult = new LoanApplicationResult();
        loanApplicationResult.setApplication(loanApplication);
        loanApplicationResult.setFinalScore(finalScore);
        loanApplicationResult.setDecision(getDecision(finalScore));

        // Save the loan application result to the database
        loanApplicationResultRepo.save(loanApplicationResult);
    }

    private Decision getDecision(double finalScore){
        if(finalScore >= 0.75){
            metrics.incrementApproved();
            return Decision.APPROVE;
        } else if(finalScore < 0.40){
            metrics.incrementRejected();
            return Decision.REJECT;
        }
        metrics.incrementReview();
        return Decision.REVIEW;
    }
}
