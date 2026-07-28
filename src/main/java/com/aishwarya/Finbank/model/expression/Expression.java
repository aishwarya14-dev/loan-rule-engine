package com.aishwarya.Finbank.model.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Jackson polymorphic deserialization configuration
//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "expressionType"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Condition.class, name = "CONDITION"),
//        @JsonSubTypes.Type(value = AndExpression.class, name = "AND"),
//        @JsonSubTypes.Type(value = OrExpression.class, name = "OR")
//})
public interface Expression {
}
