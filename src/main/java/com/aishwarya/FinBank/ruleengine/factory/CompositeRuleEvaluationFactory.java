package com.aishwarya.FinBank.ruleengine.factory;

import com.aishwarya.FinBank.ruleengine.evaluation.CompositeRuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.RuleEvaluation;
import com.aishwarya.FinBank.ruleengine.evaluation.SimpleRuleEvaluation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CompositeRuleEvaluationFactory {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SimpleRuleEvaluationFactory factory;

    public RuleEvaluation fromJson(JsonNode jsonNode){
        String type = jsonNode.has("type") ? jsonNode.get("type").asText() : "SIMPLE";

        if(type.equals("COMPOSITE")){
            CompositeRuleEvaluation compositeRuleEvaluation = context.getBean(CompositeRuleEvaluation.class);
            compositeRuleEvaluation.setLogic(jsonNode.get("logic").asText());
            List<RuleEvaluation> children = new ArrayList<>();
            for(JsonNode child : jsonNode.get("condition").get("conditions")){
                children.add(fromJson(child));
            }
            compositeRuleEvaluation.setRuleConditions(children);
            return compositeRuleEvaluation;
        }else{
            RuleEvaluation simpleRuleEvaluationObject = factory.createSimpleCondition(jsonNode.get("field").asText(), jsonNode.get("operator").asText(), jsonNode.get("value").asText());
            return simpleRuleEvaluationObject;
        }
    }
}
