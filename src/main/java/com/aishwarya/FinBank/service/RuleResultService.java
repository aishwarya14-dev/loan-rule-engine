package com.aishwarya.FinBank.service;

import com.aishwarya.FinBank.ruleengine.model.RuleResult;
import com.aishwarya.FinBank.ruleengine.repository.RuleResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleResultService {

    @Autowired
    private RuleResultRepo ruleResultRepo;

    public void saveRuleResult(RuleResult result) {
        ruleResultRepo.save(result);
    }
}
