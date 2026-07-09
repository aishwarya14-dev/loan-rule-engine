grammar LoanRules;

options {
    visitor = true;
    listener = true;
    caseInsensitive = true;
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
    | BOOLEAN         # booleanValue
    | DATETIME        # dateTimeValue
    | DATE            # dateValue
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


NUMBER     : [0-9]+ ;
DECIMAL    : [0-9]+ '.' [0-9]+ ;
WS         : [ \t\r\n]+ -> skip ;
BOOLEAN    : 'true' | 'false';
IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]* ;
DATETIME
    : '\'' NUMBER NUMBER NUMBER NUMBER '-'
         NUMBER NUMBER '-'
         NUMBER NUMBER
         'T'
         NUMBER NUMBER ':'
         NUMBER NUMBER ':'
         NUMBER NUMBER '\''
    ;
DATE
    : '\'' NUMBER NUMBER NUMBER NUMBER '-'
         NUMBER NUMBER '-'
         NUMBER NUMBER '\''
    ;
STRING_LITERAL : '\'' (~['\r\n])* '\'';
