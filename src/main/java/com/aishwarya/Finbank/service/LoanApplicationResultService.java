package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.enums.Decision;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.LoanApplicationResultRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class LoanApplicationResultService {

    private LoanApplicationResultRepo loanApplicationResultRepo;

    public void calculateAndSaveLoanApplicationResult(List<RuleResult> ruleResultList, LoanApplication loanApplication,boolean isDynamic){
        double finalScore = 0.0;
        Map<Factor,Integer> factorMap = new ConcurrentHashMap<Factor,Integer>();
        if(!isDynamic){
            finalScore = calculateFinalScoreForStaticRuleSet(ruleResultList);
        }
        else{
            Integer totalWeight = storeFactorMapping(ruleResultList,factorMap);
            finalScore = calculateFinalScoreForDynamicRuleset(ruleResultList,totalWeight,factorMap);
        }
        saveLoanApplicationResult(loanApplication,finalScore);
    }

    private double calculateFinalScoreForStaticRuleSet(List<RuleResult> ruleResultList){
        double finalScore = 0.0;
        for(RuleResult ruleResult : ruleResultList){
            finalScore += ruleResult.getRuleEvaluationScore();
        }
        return finalScore;
    }

    public Integer storeFactorMapping(List<RuleResult> ruleResultList,Map<Factor,Integer> factorMap){
        Integer totalWeight = 0;
        for(RuleResult ruleResult: ruleResultList){
            LoanTypeFactorConfig loanTypeFactorConfig = ruleResult.getLoanTypeFactorConfig();
            Integer weight = loanTypeFactorConfig.getImportanceLevel().getWeight();
            totalWeight += weight;
            Factor factor = loanTypeFactorConfig.getFactor();
            Integer currentWeight = factorMap.getOrDefault(factor,0);
            factorMap.put(factor, currentWeight+weight);
        }
        return totalWeight;
    }

    private double calculateFinalScoreForDynamicRuleset(List<RuleResult> ruleResultList, Integer totalWeight, Map<Factor,Integer> factorMap){
        double finalScore = 0.0;
        for(RuleResult ruleResult : ruleResultList){
            Factor factor = ruleResult.getLoanTypeFactorConfig().getFactor();
            finalScore +=  ruleResult.getRuleEvaluationScore() * ((double) factorMap.get(factor) / totalWeight);
        }
        return finalScore;
    }

    private void saveLoanApplicationResult(LoanApplication loanApplication,double finalScore){
        LoanApplicationResult loanApplicationResult = new LoanApplicationResult();
        loanApplicationResult.setApplication(loanApplication);
        loanApplicationResult.setFinalScore(finalScore);
        loanApplicationResult.setDecision(getDecision(finalScore));

        loanApplicationResultRepo.save(loanApplicationResult);
    }

    public Decision getDecision(double finalScore){
        if(finalScore >= 0.75){
            return Decision.APPROVE;
        } else if(finalScore < 0.40){
            return Decision.REJECT;
        }
        return Decision.REVIEW;
    }
}
