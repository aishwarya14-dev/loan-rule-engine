grammar LoanRules;

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
    : NUMBER          # intValue
    | DECIMAL         # decimalValue
    | STRING_LITERAL  # stringValue
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
DECIMAL    : [0-9]+ '.' [0-9]+ ;
STRING_LITERAL : '\'' [a-zA-Z0-9_]+ '\'' ;
WS         : [ \t\r\n]+ -> skip ;
