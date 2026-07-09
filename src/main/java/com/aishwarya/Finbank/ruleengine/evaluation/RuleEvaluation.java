package com.aishwarya.Finbank.ruleengine.evaluation;

import com.aishwarya.Finbank.enums.Logic;
import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.model.Rule;
import com.aishwarya.Finbank.model.RuleResult;
import com.aishwarya.Finbank.model.expression.AndExpression;
import com.aishwarya.Finbank.model.expression.Condition;
import com.aishwarya.Finbank.model.expression.Expression;
import com.aishwarya.Finbank.model.expression.OrExpression;

import java.util.List;


public interface RuleEvaluation {
    RuleResult evaluate(LoanApplication application, Rule rule);
}
