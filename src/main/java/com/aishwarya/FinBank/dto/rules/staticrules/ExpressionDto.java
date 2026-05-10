package com.aishwarya.FinBank.dto.rules.staticrules;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "expressionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConditionDto.class,name = "CONDITION"),
        @JsonSubTypes.Type(value = AndExpressionDto.class, name = "AND"),
        @JsonSubTypes.Type(value = OrExpressionDto.class,  name = "OR")
})
public interface ExpressionDto {}
