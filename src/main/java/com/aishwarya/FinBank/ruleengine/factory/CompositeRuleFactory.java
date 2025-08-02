package com.aishwarya.FinBank.ruleengine.factory;

import com.aishwarya.FinBank.ruleengine.condition.CompositeRuleCondition;
import com.aishwarya.FinBank.ruleengine.condition.RuleCondition;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class CompositeRuleFactory {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private SimpleRuleFactory factory;

    public RuleCondition fromJson(JsonNode jsonNode){
        String type = jsonNode.has("type") ? jsonNode.get("type").asText() : "SIMPLE";

        if(type.equals("COMPOSITE")){
            CompositeRuleCondition compositeRuleCondition = context.getBean(CompositeRuleCondition.class);
            compositeRuleCondition.setLogic(jsonNode.get("logic").asText());
            List<RuleCondition> children = new ArrayList<>();
            for(JsonNode child : jsonNode.get("condition").get("conditions")){
                children.add(fromJson(child));
            }
            compositeRuleCondition.setRuleConditions(children);
            return compositeRuleCondition;
        }else{
            RuleCondition simpleRuleCondition = factory.createSimpleCondition(jsonNode.get("field").asText(), jsonNode.get("operator").asText(), jsonNode.get("value").asText());
            return simpleRuleCondition;
        }
    }
}
