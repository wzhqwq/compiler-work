package org.wzhqwq.syntax.parser;

import org.wzhqwq.enums.SymbolIds;
import org.wzhqwq.syntax.LL1Builder;
import org.wzhqwq.syntax.production.Production;
import org.wzhqwq.syntax.production.literal.LiteralNonTerminalSymbol;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.literal.LiteralTerminalSymbol;
import org.wzhqwq.syntax.symbol.*;

public class SyntaxParser {
    // 操作符
    private static final LiteralTerminalSymbol ASSIGN = new LiteralTerminalSymbol(SymbolIds.ASSIGN);
    private static final LiteralTerminalSymbol ARITHMETIC_L1 = new LiteralTerminalSymbol(SymbolIds.ARITHMETIC_L1);
    private static final LiteralTerminalSymbol ARITHMETIC_L2 = new LiteralTerminalSymbol(SymbolIds.ARITHMETIC_L2);
    private static final LiteralTerminalSymbol RELATIONAL = new LiteralTerminalSymbol(SymbolIds.RELATIONAL_PARTIAL);
    private static final LiteralTerminalSymbol EQUAL = new LiteralTerminalSymbol(SymbolIds.EQUAL);

    // 界符
    private static final LiteralTerminalSymbol SEMICOLON = new LiteralTerminalSymbol(SymbolIds.SEMICOLON);
    private static final LiteralTerminalSymbol COMMA = new LiteralTerminalSymbol(SymbolIds.COMMA);
    private static final LiteralTerminalSymbol BRACKET_LEFT = new LiteralTerminalSymbol(SymbolIds.BRACKET_LEFT);
    private static final LiteralTerminalSymbol BRACKET_RIGHT = new LiteralTerminalSymbol(SymbolIds.BRACKET_RIGHT);
    private static final LiteralTerminalSymbol DOT = new LiteralTerminalSymbol(SymbolIds.DOT);

    // 关键字
    private static final LiteralTerminalSymbol CONST = new LiteralTerminalSymbol(SymbolIds.CONST_KEYWORD);
    private static final LiteralTerminalSymbol VAR = new LiteralTerminalSymbol(SymbolIds.VAR_KEYWORD);
    private static final LiteralTerminalSymbol PROCEDURE = new LiteralTerminalSymbol(SymbolIds.PROCEDURE_KEYWORD);
    private static final LiteralTerminalSymbol BEGIN = new LiteralTerminalSymbol(SymbolIds.BEGIN_KEYWORD);
    private static final LiteralTerminalSymbol END = new LiteralTerminalSymbol(SymbolIds.END_KEYWORD);
    private static final LiteralTerminalSymbol IF = new LiteralTerminalSymbol(SymbolIds.IF_KEYWORD);
    private static final LiteralTerminalSymbol THEN = new LiteralTerminalSymbol(SymbolIds.THEN_KEYWORD);
    private static final LiteralTerminalSymbol ODD = new LiteralTerminalSymbol(SymbolIds.ODD_KEYWORD);
    private static final LiteralTerminalSymbol CALL = new LiteralTerminalSymbol(SymbolIds.CALL_KEYWORD);
    private static final LiteralTerminalSymbol WHILE = new LiteralTerminalSymbol(SymbolIds.WHILE_KEYWORD);
    private static final LiteralTerminalSymbol DO = new LiteralTerminalSymbol(SymbolIds.DO_KEYWORD);
    private static final LiteralTerminalSymbol READ = new LiteralTerminalSymbol(SymbolIds.READ_KEYWORD);
    private static final LiteralTerminalSymbol WRITE = new LiteralTerminalSymbol(SymbolIds.WRITE_KEYWORD);

    // 其他非终结符号
    private static final LiteralTerminalSymbol IDENTIFIER = new LiteralTerminalSymbol(SymbolIds.IDENTIFIER);
    private static final LiteralTerminalSymbol NUMBER = new LiteralTerminalSymbol(SymbolIds.NUMBER);
    private static final LiteralTerminalSymbol EPSILON = LiteralTerminalSymbol.EPSILON;
    private static final LiteralTerminalSymbol EOF = new LiteralTerminalSymbol(SymbolIds.EOF);

    private LL1Builder.LL1Predictor predictor;

    public void initializePredictor() {
        // 程序结构相关
        final LiteralNonTerminalSymbol M = new LiteralNonTerminalSymbol(SymbolIds.M) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new MSymbol();
            }
        };
        final LiteralNonTerminalSymbol program = new LiteralNonTerminalSymbol(SymbolIds.PROGRAM) {
            @Override
            public String getMismatchMessage(SymbolIds symbolId) {
                if (symbolId == SymbolIds.EOF) {
                    return "没有意料到的代码结尾";
                }
                return null;
            }
        };
        final LiteralNonTerminalSymbol programBody = new LiteralNonTerminalSymbol(SymbolIds.PROGRAM_BODY);
        final LiteralNonTerminalSymbol constDeclaration = new LiteralNonTerminalSymbol(SymbolIds.CONST_DECLARATION);
        final LiteralNonTerminalSymbol constDefinition = new LiteralNonTerminalSymbol(SymbolIds.CONST_DEFINITION);
        final LiteralNonTerminalSymbol constDefinitionsPart1 = new LiteralNonTerminalSymbol(SymbolIds.CONST_DEFINITIONS_1);
        final LiteralNonTerminalSymbol constDefinitionsPart2 = new LiteralNonTerminalSymbol(SymbolIds.CONST_DEFINITIONS_2);
        final LiteralNonTerminalSymbol varDeclaration = new LiteralNonTerminalSymbol(SymbolIds.VAR_DECLARATION);
        final LiteralNonTerminalSymbol identifiersPart1 = new LiteralNonTerminalSymbol(SymbolIds.IDENTIFIERS_1);
        final LiteralNonTerminalSymbol identifiersPart2 = new LiteralNonTerminalSymbol(SymbolIds.IDENTIFIERS_2);
        final LiteralNonTerminalSymbol proceduresPart1 = new LiteralNonTerminalSymbol(SymbolIds.PROCEDURES_1);
        final LiteralNonTerminalSymbol proceduresPart2 = new LiteralNonTerminalSymbol(SymbolIds.PROCEDURES_2);
        final LiteralNonTerminalSymbol procedureHead = new LiteralNonTerminalSymbol(SymbolIds.PROCEDURE_HEAD);

        // 表达式相关
        final LiteralNonTerminalSymbol condition = new LiteralNonTerminalSymbol(SymbolIds.CONDITION) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new ConditionSymbol();
            }
        };
        final LiteralNonTerminalSymbol relational = new LiteralNonTerminalSymbol(SymbolIds.RELATIONAL) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new OperationSymbol(SymbolIds.RELATIONAL);
            }
        };
        final LiteralNonTerminalSymbol expression = new LiteralNonTerminalSymbol(SymbolIds.EXPRESSION);
        final LiteralNonTerminalSymbol sign = new LiteralNonTerminalSymbol(SymbolIds.SIGN) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new OperationSymbol(SymbolIds.SIGN);
            }
        };
        final LiteralNonTerminalSymbol termsPart1 = new LiteralNonTerminalSymbol(SymbolIds.TERMS_1);
        final LiteralNonTerminalSymbol termsPart2 = new LiteralNonTerminalSymbol(SymbolIds.TERMS_2);
        final LiteralNonTerminalSymbol term = new LiteralNonTerminalSymbol(SymbolIds.TERM);
        final LiteralNonTerminalSymbol factorsPart = new LiteralNonTerminalSymbol(SymbolIds.FACTORS);
        final LiteralNonTerminalSymbol factor = new LiteralNonTerminalSymbol(SymbolIds.FACTOR);

        // 语句相关
        final LiteralNonTerminalSymbol statement = new LiteralNonTerminalSymbol(SymbolIds.STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol statementsPart1 = new LiteralNonTerminalSymbol(SymbolIds.STATEMENTS_1) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.STATEMENTS_1);
            }
        };
        final LiteralNonTerminalSymbol statementsPart2 = new LiteralNonTerminalSymbol(SymbolIds.STATEMENTS_2) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementExSymbol(SymbolIds.STATEMENTS_2);
            }
        };
        final LiteralNonTerminalSymbol assignStatement = new LiteralNonTerminalSymbol(SymbolIds.ASSIGN_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.ASSIGN_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol compoundStatement = new LiteralNonTerminalSymbol(SymbolIds.COMPOUND_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.COMPOUND_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol ifStatement = new LiteralNonTerminalSymbol(SymbolIds.IF_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.IF_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol whileStatement = new LiteralNonTerminalSymbol(SymbolIds.WHILE_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.WHILE_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol callStatement = new LiteralNonTerminalSymbol(SymbolIds.CALL_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.CALL_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol readStatement = new LiteralNonTerminalSymbol(SymbolIds.READ_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.READ_STATEMENT);
            }
        };
        final LiteralNonTerminalSymbol writeStatement = new LiteralNonTerminalSymbol(SymbolIds.WRITE_STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.WRITE_STATEMENT);
            }
        };

        // 产生式及其语法规则
        M.addProduction(new LiteralSymbol[]{ EPSILON }, (left, right, env) -> {
            ((MSymbol) left).nextPtr = env.codeList.getCodePtr();
        });
        Production root = program.addProduction(new LiteralSymbol[]{ programBody, DOT });
    }
}
