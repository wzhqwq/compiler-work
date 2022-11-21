package org.wzhqwq.enums;

public enum SymbolIds {
    // 终结符号
    // 关键字
    CONST_KEYWORD,
    VAR_KEYWORD,
    PROCEDURE_KEYWORD,
    BEGIN_KEYWORD,
    END_KEYWORD,
    IF_KEYWORD,
    THEN_KEYWORD,
    ODD_KEYWORD,
    CALL_KEYWORD,
    WHILE_KEYWORD,
    DO_KEYWORD,
    READ_KEYWORD,
    WRITE_KEYWORD,

    // 操作符
    ASSIGN,
    ARITHMETIC_L1,
    ARITHMETIC_L2,
    RELATIONAL_PARTIAL,
    EQUAL,

    // 界符
    SEMICOLON,
    COMMA,
    BRACKET_LEFT,
    BRACKET_RIGHT,
    DOT,

    // 标识符和数字
    IDENTIFIER,
    NUMBER,

    // epsilon
    EPSILON,

    // 结束符
    EOF,

    // 非终结符号
    PROGRAM,
    PROGRAM_BODY,
    PROCEDURES_1,
    PROCEDURES_2,
    PROCEDURE_HEAD,

    CONST_DECLARATION,
    CONST_DEFINITIONS_1,
    CONST_DEFINITIONS_2,
    CONST_DEFINITION,
    VAR_DECLARATION,
    IDENTIFIERS_1,
    IDENTIFIERS_2,

    STATEMENT,
    STATEMENTS_1,
    STATEMENTS_2,
    ASSIGN_STATEMENT,
    IF_STATEMENT,
    WHILE_STATEMENT,
    CALL_STATEMENT,
    READ_STATEMENT,
    WRITE_STATEMENT,
    COMPOUND_STATEMENT,

    EXPRESSION,
    TERMS_1,
    TERMS_2,
    TERM,
    FACTORS,
    FACTOR,
    CONDITION,

    RELATIONAL,
    SIGN,
    M,
    JUMPER,
}
