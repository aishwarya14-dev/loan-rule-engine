package com.aishwarya.Finbank.controller;

import com.aishwarya.Finbank.dto.rules.dynamicrules.RulesRequestDto;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rules")
public class RulesController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/dsl")
    public ResponseEntity<DslRule> acceptRules(@Valid @RequestBody RulesRequestDto rulesRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ruleService.save(rulesRequestDto));
    }
}
