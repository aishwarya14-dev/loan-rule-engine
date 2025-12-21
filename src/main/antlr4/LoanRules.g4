grammar LoanRules;

//@header {
//package com.aishwarya.FinBank.antlr4;
//}

options {
    visitor = true;
    listener = true;
}

statement
    : 'IF' expression 'THEN' action EOF
    ;

expression
    : expression 'OR' expression     # orExpression
    | expression 'AND' expression    # andExpression
    | '(' expression ')'             # parenExpression
    | condition                      # conditionExpression
    ;

condition
    : IDENTIFIER operator value
    ;

operator
    : GT
    | GE
    | LT
    | LE
    | EQ
    | NEQ
    ;

value
    : NUMBER
    ;

action
    : APPROVE
    | REJECT
    | REVIEW
    ;

APPROVE : 'approve';
REJECT  : 'reject';
REVIEW : 'review';

GT      : '>';
GE      : '>=';
LT      : '<';
LE      : '<=';
EQ      : '==';
NEQ     : '!=';

AND     : 'AND';
OR      : 'OR';

IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER     : [0-9]+ ;
WS         : [ \t\r\n]+ -> skip ;
