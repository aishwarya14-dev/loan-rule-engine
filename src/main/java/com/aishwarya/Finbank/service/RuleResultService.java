package com.aishwarya.Finbank.service;


import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.repository.RuleResultRepo;
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
