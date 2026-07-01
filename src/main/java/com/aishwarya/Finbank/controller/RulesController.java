package com.aishwarya.Finbank.controller;

import com.aishwarya.Finbank.dto.rules.dynamicrules.RulesRequestDto;
import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.service.RuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rules")
@Tag(name = "Rules APIs" , description = "Supports Rule Creation")
public class RulesController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/dsl")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DslRule> acceptRules(@Valid @RequestBody RulesRequestDto rulesRequestDto) {
        log.info("POST /dsl - rule={}", rulesRequestDto.getDslRule());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ruleService.save(rulesRequestDto));
    }
}
