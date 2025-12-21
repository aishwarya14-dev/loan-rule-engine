package com.aishwarya.FinBank.ruleengine.model.condition;

import com.aishwarya.FinBank.ruleengine.model.Logic;

import java.util.List;

public final class CompositeCondition implements Condition {
    private Logic logic;
    private List<Condition> conditions;

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
}
