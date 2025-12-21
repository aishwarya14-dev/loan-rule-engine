package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RulesController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/rules/dsl")
    public void acceptRules(@RequestBody RulesRequestDto rulesRequestDto) {
        ruleService.processRules(rulesRequestDto);

    }
}
