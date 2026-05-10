package com.aishwarya.FinBank.dto.rules.staticrules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrExpressionDto implements ExpressionDto{
    private ExpressionDto left;
    private ExpressionDto right;

    public OrExpressionDto(ExpressionDto left, ExpressionDto right) {
        this.left = left;
        this.right = right;
    }

    public ExpressionDto getLeft() {
        return left;
    }

    public ExpressionDto getRight() {
        return right;
    }
}
