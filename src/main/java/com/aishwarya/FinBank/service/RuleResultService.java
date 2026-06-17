package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.model.RuleResult;
import com.aishwarya.FinBank.repository.RuleResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleResultService {

    @Autowired
    private RuleResultRepo ruleResultRepo;

    public void saveRuleResult(RuleResult result) {
        ruleResultRepo.save(result);
    }
}
