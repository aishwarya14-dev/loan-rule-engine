package com.aishwarya.FinBank.ruleengine.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class Rule {
    private Condition condition;
    private Action action;
    private Logic logic;
    private String type;


    public Condition getCondition() {
        return condition;
    }

    public Action getAction() {
        return action;
    }

    public Logic getLogic() {
        return logic;
    }

    public String getType() {
        return type;
    }
}
