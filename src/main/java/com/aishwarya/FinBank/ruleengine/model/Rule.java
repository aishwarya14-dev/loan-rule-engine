package com.aishwarya.FinBank.ruleengine.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class Rule {
    private Condition condition;
    private Action action;
    private String logic;
    private String type;

    public Rule(Condition condition, Action action, String logic, String type) {
        this.condition = condition;
        this.action = action;
        this.logic = logic;
        this.type = type;
    }


    public Condition getCondition() {
        return condition;
    }

    public Action getAction() {
        return action;
    }

    public String getLogic() {
        return logic;
    }

    public String getType() {
        return type;
    }
}

