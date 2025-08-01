package com.aishwarya.FinBank.utility;

public enum Operator {
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    EQ("=="),
    NE("!=");

    private String symbol;

    Operator(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Operator fromSymbol(String symbol){
        for(Operator op : Operator.values()){
            if(op.symbol.equals(symbol)){
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + symbol);
    }
}
