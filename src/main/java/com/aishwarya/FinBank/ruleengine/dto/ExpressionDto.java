package com.aishwarya.FinBank.ruleengine.dto;


import com.aishwarya.FinBank.ruleengine.model.condition.AndExpression;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.aishwarya.FinBank.ruleengine.model.condition.OrExpression;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "expressionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Condition.class,name = "CONDITION"),
        @JsonSubTypes.Type(value = AndExpression.class, name = "AND"),
        @JsonSubTypes.Type(value = OrExpression.class,  name = "OR")
})
public interface ExpressionDto {}
