package com.aishwarya.FinBank.controller;

import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.ruleengine.model.DslRule;
import com.aishwarya.FinBank.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/dsl")
    public ResponseEntity<DslRule> acceptRules(@RequestBody RulesRequestDto rulesRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ruleService.save(rulesRequestDto));
    }
}
