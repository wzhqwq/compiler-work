package org.wzhqwq.syntax.parser;

import org.wzhqwq.enums.Instructions;
import org.wzhqwq.enums.OperationTypes;
import org.wzhqwq.enums.SymbolIds;
import org.wzhqwq.exception.SyntaxException;
import org.wzhqwq.lexical.symbol.IdentifierSymbol;
import org.wzhqwq.lexical.symbol.NumberSymbol;
import org.wzhqwq.lexical.symbol.OperatorSymbol;
import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.syntax.LL1Builder;
import org.wzhqwq.syntax.production.Production;
import org.wzhqwq.syntax.production.literal.LiteralNonTerminalSymbol;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.literal.LiteralTerminalSymbol;
import org.wzhqwq.syntax.symbol.*;

import java.util.List;

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

    private LL1Builder.LL1Predictor predictor;

    public void initializePredictor() {
        // 非终结符配置
        // region 程序结构相关
        final LiteralNonTerminalSymbol M = new LiteralNonTerminalSymbol(SymbolIds.M) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new MSymbol();
            }
        };
        final LiteralNonTerminalSymbol jumper = new LiteralNonTerminalSymbol(SymbolIds.JUMPER) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new JumperSymbol();
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
        final LiteralNonTerminalSymbol programBody = new LiteralNonTerminalSymbol(SymbolIds.PROGRAM_BODY) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new ProgramBodySymbol();
            }
        };
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
        // endregion

        // region 表达式相关
        final LiteralNonTerminalSymbol condition = new LiteralNonTerminalSymbol(SymbolIds.CONDITION) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new ConditionSymbol();
            }

            @Override
            public String getMismatchMessage(SymbolIds symbolId) {
                return symbolId.toString() + "不属于条件的开头";
            }
        };
        final LiteralNonTerminalSymbol relational = new LiteralNonTerminalSymbol(SymbolIds.RELATIONAL) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new OperationSymbol(SymbolIds.RELATIONAL);
            }
        };
        final LiteralNonTerminalSymbol expression = new LiteralNonTerminalSymbol(SymbolIds.EXPRESSION) {
            @Override
            public String getMismatchMessage(SymbolIds symbolId) {
                return symbolId.toString() + "不属于表达式的开头";
            }
        };
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
        // endregion

        // region 语句相关
        final LiteralNonTerminalSymbol statement = new LiteralNonTerminalSymbol(SymbolIds.STATEMENT) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.STATEMENT);
            }

            @Override
            public String getMismatchMessage(SymbolIds symbolId) {
                return symbolId.toString() + "不属于语句的开头";
            }
        };
        final LiteralNonTerminalSymbol statementsPart1 = new LiteralNonTerminalSymbol(SymbolIds.STATEMENTS_1) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementSymbol(SymbolIds.STATEMENTS_1);
            }

            @Override
            public String getMismatchMessage(SymbolIds symbolId) {
                if (symbolId == SymbolIds.END_KEYWORD) return "复合语句中最后一条语句后不能有分号";
                return symbolId.toString() + "不属于语句的开头";
            }
        };
        final LiteralNonTerminalSymbol statementsPart2 = new LiteralNonTerminalSymbol(SymbolIds.STATEMENTS_2) {
            @Override
            public NonTerminalSymbol toNonTerminalSymbol() {
                return new StatementExSymbol(SymbolIds.STATEMENTS_2);
            }
        };
        // endregion

        // 产生式及其语法规则
        M.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> ((MSymbol) left).nextPtr = env.codeList.getCodePtr()
        );
        jumper.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> ((JumperSymbol) left).jumpList = env.codeList.pushPartialCode(Instructions.JMP)
        );
        Production root = program.addProduction(
                new LiteralSymbol[]{ jumper, programBody, DOT },
                (left, right, env) -> env.codeList.fill(((JumperSymbol) right[0]).jumpList, ((ProgramBodySymbol) right[1]).entryPoint)
        );
        programBody.addProduction(
                new LiteralSymbol[]{ constDeclaration, varDeclaration, proceduresPart1, M, statement },
                (left, right, env) -> {
                    env.codeList.fill(((StatementSymbol) right[4]).nextList, env.codeList.getCodePtr());
                    env.codeList.pushOpr(OperationTypes.EXIT);
                    env.table.leaveProcedure();
                    ((ProgramBodySymbol) left).entryPoint = ((MSymbol) right[3]).nextPtr;
                }
        );

        // region 常量声明
        constDeclaration.addProduction(new LiteralSymbol[]{ CONST, constDefinitionsPart1, SEMICOLON });
        constDeclaration.addProduction(new LiteralSymbol[]{ EPSILON });
        constDefinitionsPart1.addProduction(new LiteralSymbol[]{ constDefinition, constDefinitionsPart2 });
        constDefinitionsPart2.addProduction(new LiteralSymbol[]{ COMMA, constDefinitionsPart1 });
        constDefinitionsPart2.addProduction(new LiteralSymbol[]{ EPSILON });
        constDefinition.addProduction(
                new LiteralSymbol[]{ IDENTIFIER, EQUAL, NUMBER },
                (left, right, env) -> env.table.addConstant(
                        ((IdentifierSymbol) right[0]).identifierName,
                        ((NumberSymbol) right[2]).value
                )
        );
        // endregion

        // region 变量声明
        varDeclaration.addProduction(
                new LiteralSymbol[]{ VAR, identifiersPart1, SEMICOLON },
                (left, right, env) -> {
                    env.codeList.pushCode(Instructions.INT, 0, env.identifiers.size() + 3);
                    for (IdentifierSymbol identifier : env.identifiers) {
                        env.table.addVariable(identifier.identifierName);
                    }
                }
        );
        varDeclaration.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> env.codeList.pushCode(Instructions.INT, 0, 3)
        );
        identifiersPart1.addProduction(
                new LiteralSymbol[]{ IDENTIFIER, identifiersPart2 },
                (left, right, env) -> env.identifiers.add((IdentifierSymbol) right[0])
        );
        identifiersPart2.addProduction(new LiteralSymbol[]{ COMMA, identifiersPart1 });
        identifiersPart2.addProduction(new LiteralSymbol[]{ EPSILON });
        // endregion

        // region 过程声明
        proceduresPart1.addProduction(
                new LiteralSymbol[]{ procedureHead, jumper, programBody, SEMICOLON, proceduresPart2 },
                (left, right, env) -> env.codeList.fill(((JumperSymbol) right[1]).jumpList, ((ProgramBodySymbol) right[2]).entryPoint)
        );
        proceduresPart1.addProduction(new LiteralSymbol[]{ EPSILON });
        proceduresPart2.addProduction(new LiteralSymbol[]{ proceduresPart1 });
        proceduresPart2.addProduction(new LiteralSymbol[]{ EPSILON });
        procedureHead.addProduction(
                new LiteralSymbol[]{ PROCEDURE, IDENTIFIER, SEMICOLON },
                (left, right, env) -> env.table.enterProcedure(
                        ((IdentifierSymbol) right[1]).identifierName,
                        env.codeList.getCodePtr()
                )
        );
        // endregion

        // region 条件表达式
        condition.addProduction(
                new LiteralSymbol[]{ expression, relational, expression },
                (left, right, env) -> {
                    env.codeList.pushOpr(((OperationSymbol) right[1]).variant);
                    ((ConditionSymbol) left).falseList = env.codeList.pushPartialCode(Instructions.JPC);
                    ((ConditionSymbol) left).trueList = env.codeList.pushPartialCode(Instructions.JMP);
                }
        );
        condition.addProduction(
                new LiteralSymbol[]{ ODD, expression },
                (left, right, env) -> {
                    env.codeList.pushOpr(OperationTypes.ODD);
                    ((ConditionSymbol) left).falseList = env.codeList.pushPartialCode(Instructions.JPC);
                    ((ConditionSymbol) left).trueList = env.codeList.pushPartialCode(Instructions.JMP);
                }
        );
        relational.addProduction(
                new LiteralSymbol[]{ RELATIONAL },
                (left, right, env) -> ((OperationSymbol) left).variant = ((OperatorSymbol) right[0]).variant
        );
        relational.addProduction(
                new LiteralSymbol[]{ EQUAL },
                (left, right, env) -> ((OperationSymbol) left).variant = OperationTypes.EQUAL
        );
        // endregion

        // region 数值表达式
        expression.addProduction(
                new LiteralSymbol[]{ sign, termsPart1 },
                (left, right, env) -> {
                    env.codeList.pushCode(Instructions.LIT, 0, 0);
                    env.codeList.pushOpr(((OperationSymbol) right[0]).variant);
                }
        );
        sign.addProduction(
                new LiteralSymbol[]{ ARITHMETIC_L1 },
                (left, right, env) -> ((OperationSymbol) left).variant = ((OperatorSymbol) right[0]).variant
        );
        sign.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> ((OperationSymbol) left).variant = OperationTypes.NONE
        );

        termsPart1.addProduction(new LiteralSymbol[]{ term, termsPart2 });
        termsPart2.addProduction(
                new LiteralSymbol[]{ ARITHMETIC_L1, termsPart1 },
                (left, right, env) -> env.codeList.pushOpr(((OperatorSymbol) right[0]).variant)
        );
        termsPart2.addProduction(new LiteralSymbol[]{ EPSILON });
        term.addProduction(new LiteralSymbol[]{ factor, factorsPart });

        factorsPart.addProduction(
                new LiteralSymbol[]{ ARITHMETIC_L2, term },
                (left, right, env) -> env.codeList.pushOpr(((OperatorSymbol) right[0]).variant)
        );
        factorsPart.addProduction(new LiteralSymbol[]{ EPSILON });
        factor.addProduction(
                new LiteralSymbol[]{ IDENTIFIER },
                (left, right, env) -> {
                    IdentifierSymbol identifier = ((IdentifierSymbol) right[0]);
                    Table.Row row = env.table.findRow(identifier.identifierName);
                    if (row == null) throw new SyntaxException("标识符不存在：" + identifier.identifierName, identifier.left, identifier.right);
                    switch (row.type) {
                        case CONSTANT -> env.codeList.pushCode(
                                Instructions.LIT,
                                0,
                                ((Table.ConstantRow) row).value
                        );
                        case VARIABLE -> env.codeList.pushCode(
                                Instructions.LOD,
                                env.table.getLevel() - ((Table.VariableRow) row).level,
                                ((Table.VariableRow) row).address
                        );
                        default -> throw new SyntaxException("此处标识符应为变量或常量", identifier.left, identifier.right);
                    }
                }
        );
        factor.addProduction(
                new LiteralSymbol[]{ NUMBER },
                (left, right, env) -> env.codeList.pushCode(Instructions.LIT, 0, ((NumberSymbol) right[0]).value)
        );
        factor.addProduction(new LiteralSymbol[]{ BRACKET_LEFT, expression, BRACKET_RIGHT });
        // endregion

        // region 简单语句
        statement.addProduction(
                new LiteralSymbol[]{ IDENTIFIER, ASSIGN, expression },
                (left, right, env) -> {
                    IdentifierSymbol identifier = ((IdentifierSymbol) right[0]);
                    Table.Row row = env.table.findRow(identifier.identifierName);
                    if (row == null) throw new SyntaxException("标识符不存在：" + identifier.identifierName, identifier.left, identifier.right);
                    if (row.type == TableRowType.VARIABLE) {
                        env.codeList.pushCode(
                                Instructions.STO,
                                env.table.getLevel() - ((Table.VariableRow) row).level,
                                ((Table.VariableRow) row).address
                        );
                    }
                    else {
                        throw new SyntaxException("赋值左值应为变量", identifier.left, identifier.right);
                    }
                    ((StatementSymbol) left).nextList = new int[] {};
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ IF, condition, THEN, M, statement },
                (left, right, env) -> {
                    env.codeList.fill(((ConditionSymbol) right[1]).trueList, ((MSymbol) right[3]).nextPtr);
                    ((StatementSymbol) left).nextList = env.codeList.merge(
                            ((ConditionSymbol) right[1]).falseList,
                            ((StatementSymbol) right[4]).nextList
                    );
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ WHILE, M, condition, DO, M, statement },
                (left, right, env) -> {
                    MSymbol M1 = ((MSymbol) right[1]);
                    MSymbol M2 = ((MSymbol) right[4]);
                    ConditionSymbol C = ((ConditionSymbol) right[2]);
                    StatementSymbol S = ((StatementSymbol) right[5]);

                    env.codeList.fill(S.nextList, M1.nextPtr);
                    env.codeList.fill(C.trueList, M2.nextPtr);
                    ((StatementSymbol) left).nextList = C.falseList;
                    env.codeList.pushCode(Instructions.JMP, 0, M1.nextPtr);
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ CALL, IDENTIFIER },
                (left, right, env) -> {
                    IdentifierSymbol identifier = ((IdentifierSymbol) right[1]);
                    Table.Row row = env.table.findRow(identifier.identifierName);
                    if (row == null) throw new SyntaxException("标识符不存在：" + identifier.identifierName, identifier.left, identifier.right);
                    if (row.type == TableRowType.PROCEDURE) {
                        env.codeList.pushCode(
                                Instructions.CAL,
                                env.table.getLevel(),
                                ((Table.ProcedureRow) row).address
                        );
                    }
                    else {
                        throw new SyntaxException("call后应为过程名", identifier.left, identifier.right);
                    }
                    ((StatementSymbol) left).nextList = new int[]{};
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ READ, BRACKET_LEFT, identifiersPart1, BRACKET_RIGHT },
                (left, right, env) -> {
                    for (IdentifierSymbol identifier : env.identifiers) {
                        env.codeList.pushOpr(OperationTypes.READ);
                        Table.Row row = env.table.findRow(identifier.identifierName);
                        if (row == null) throw new SyntaxException("标识符不存在：" + identifier.identifierName, identifier.left, identifier.right);
                        if (row.type == TableRowType.VARIABLE) {
                            env.codeList.pushCode(
                                    Instructions.STO,
                                    env.table.getLevel() - ((Table.VariableRow) row).level,
                                    ((Table.VariableRow) row).address
                            );
                        }
                        else {
                            throw new SyntaxException("参数表内应是变量", identifier.left, identifier.right);
                        }
                    }
                    ((StatementSymbol) left).nextList = new int[] {};
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ WRITE, BRACKET_LEFT, identifiersPart1, BRACKET_RIGHT },
                (left, right, env) -> {
                    for (IdentifierSymbol identifier : env.identifiers) {
                        Table.Row row = env.table.findRow(identifier.identifierName);
                        if (row == null) throw new SyntaxException("标识符不存在：" + identifier.identifierName, identifier.left, identifier.right);
                        if (row.type == TableRowType.VARIABLE) {
                            env.codeList.pushCode(
                                    Instructions.LOD,
                                    env.table.getLevel() - ((Table.VariableRow) row).level,
                                    ((Table.VariableRow) row).address
                            );
                        }
                        else {
                            throw new SyntaxException("参数表内应是变量", identifier.left, identifier.right);
                        }
                        env.codeList.pushOpr(OperationTypes.WRITE);
                    }
                    ((StatementSymbol) left).nextList = new int[] {};
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> ((StatementSymbol) left).nextList = new int[] {}
        );
        // endregion

        // region 复合语句
        statementsPart1.addProduction(
                new LiteralSymbol[]{ statement, statementsPart2 },
                (left, right, env) -> {
                    StatementSymbol s = (StatementSymbol) right[0];
                    StatementSymbol s1 = (StatementSymbol) left;
                    StatementExSymbol s2 = (StatementExSymbol) right[1];

                    if (s2.nextPtr == -1) {
                        s1.nextList = s.nextList;
                    }
                    else {
                        env.codeList.fill(s.nextList, s2.nextPtr);
                        s1.nextList = s2.nextList;
                    }
                }
        );
        statementsPart2.addProduction(
                new LiteralSymbol[]{ SEMICOLON, M, statementsPart1 },
                (left, right, env) -> {
                    StatementExSymbol s2 = (StatementExSymbol) left;
                    s2.nextPtr = ((MSymbol) right[1]).nextPtr;
                    s2.nextList = ((StatementSymbol) right[2]).nextList;
                }
        );
        statementsPart2.addProduction(
                new LiteralSymbol[]{ EPSILON },
                (left, right, env) -> {
                    StatementExSymbol s2 = (StatementExSymbol) left;
                    s2.nextPtr = -1;
                    s2.nextList = new int[]{};
                }
        );
        statement.addProduction(
                new LiteralSymbol[]{ BEGIN, statementsPart1, END },
                (left, right, env) -> ((StatementSymbol) left).nextList = ((StatementSymbol) right[1]).nextList
        );
        // endregion

        predictor = new LL1Builder(root, new LiteralNonTerminalSymbol[]{
                M, jumper, program, programBody, constDeclaration, constDefinitionsPart1, constDefinitionsPart2,
                constDefinition, varDeclaration, identifiersPart1, identifiersPart2, proceduresPart1, proceduresPart2,
                procedureHead, condition, relational, expression, sign, termsPart1, termsPart2, term, factorsPart,
                factor, statement, statementsPart1, statementsPart2
        }).build();
    }

    static public class ParseResult {
        public List<CodeList.Code> codeList;
        public List<Table.Row> table;

        public ParseResult(ParsingEnv env) {
            this.codeList = env.codeList.codeList;
            this.table = env.table.table;
        }
    }

    public ParseResult parse(List<TerminalSymbol> symbols) throws SyntaxException {
        if (predictor == null) {
            initializePredictor();
        }
        ParsingEnv env = new ParsingEnv();
        int endPos = symbols.get(symbols.size() - 1).right;
        symbols.add(new TerminalSymbol(SymbolIds.EOF, "代码结束", endPos, endPos));
        for (TerminalSymbol symbol : symbols) {
            predictor.go(symbol, env);
        }
        return new ParseResult(env);
    }
}
