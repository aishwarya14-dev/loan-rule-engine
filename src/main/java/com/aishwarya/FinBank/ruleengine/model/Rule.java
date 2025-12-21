package com.aishwarya.FinBank.ruleengine.model;

import com.aishwarya.FinBank.ruleengine.model.condition.Condition;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Rule {
    private final Condition condition;
    private final Action action;
    private final Logic logic;
    private final RuleType type;

    public Rule(Condition condition, Action action, Logic logic, RuleType type) {
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

    public Logic getLogic() {
        return logic;
    }

    public RuleType getType() {
        return type;
    }
}

