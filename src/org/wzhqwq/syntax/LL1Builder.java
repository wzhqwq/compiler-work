package org.wzhqwq.syntax;

import org.wzhqwq.exception.SyntaxException;
import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.enums.SymbolIds;
import org.wzhqwq.syntax.parser.ParsingEnv;
import org.wzhqwq.syntax.production.literal.LiteralNonTerminalSymbol;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.literal.LiteralTerminalSymbol;
import org.wzhqwq.syntax.production.Production;
import org.wzhqwq.syntax.symbol.NonTerminalSymbol;

import java.util.*;

public class LL1Builder {
    private final LiteralNonTerminalSymbol[] nonTerminalSymbols;
    private final Production rootProduction;

    public LL1Builder(Production rootProduction, LiteralNonTerminalSymbol[] nonTerminalSymbols) {
        this.rootProduction = rootProduction;
        this.nonTerminalSymbols = nonTerminalSymbols;
    }

    public LL1Predictor build() {
        // 初始化first集（间接计算）和follow集
        // 开始符号的follow集包含结束标记
        rootProduction.getLeft().addFollowSet(new LiteralTerminalSymbol(SymbolIds.EOF));
        // 在所有产生式右侧推导follow集，其中非终结符前方可能是epsilon，需要加入到依赖集合中，以便迭代计算
        for (LiteralNonTerminalSymbol symbol : nonTerminalSymbols) {
            for (Production production : symbol.getProductions()) {
                production.deriveFollowSetsOfRight();
            }
        }
        // 迭代计算follow集
        deriveAllFollowSet();

//        for (LiteralNonTerminalSymbol symbol : nonTerminalSymbols) {
//            symbol.print();
//        }
        Map<SymbolIds, Map<SymbolIds, Production> > table = new HashMap<>();
        for (LiteralNonTerminalSymbol symbol : nonTerminalSymbols) {
            Map<SymbolIds, Production> map = new HashMap<>();
            for (Production production : symbol.getProductions()) {
                for (LiteralTerminalSymbol terminalSymbol : production.getSelectSet()) {
                    map.put(terminalSymbol.getId(), production);
                }
            }
            table.put(symbol.getId(), map);
        }
        return new LL1Predictor(table, rootProduction);
    }

    private void deriveAllFollowSet() {
        while (true) {
            boolean changed = false;
            for (LiteralNonTerminalSymbol nonTerminalSymbol : nonTerminalSymbols) {
                changed |= nonTerminalSymbol.updateFollowSetOnce();
            }
            if (!changed) {
                // 不断循环直到所有产生式的follow集不再变化
                break;
            }
        }
    }

    public static class LL1Predictor {
        private final Map<SymbolIds, Map<SymbolIds, Production> > table;
        private final Stack<ASTNode> stack = new Stack<>();

        public LL1Predictor(Map<SymbolIds, Map<SymbolIds, Production> > table, Production rootProduction) {
            this.table = table;
            stack.push(new ASTNode(rootProduction));
        }

        public void go(TerminalSymbol symbol, ParsingEnv env) throws SyntaxException {
            if (stack.isEmpty()) {
                if (symbol.id == SymbolIds.EOF) {
                    return;
                }
                throw new SyntaxException("预期代码已经结束，存在程序外的代码", symbol.left, symbol.right);
            }
            ASTNode node = stack.peek();
            LiteralSymbol nowSymbol = node.goForward(symbol);
            while (true) {
                if (nowSymbol.getId() == SymbolIds.EPSILON) {
                    node = popIsFinished(node, env);
                    nowSymbol = node.goForward(symbol);
                }
                if (nowSymbol instanceof LiteralTerminalSymbol) {
                    if (nowSymbol.getId() != symbol.id) {
                        throw node.getMismatchException("没有意料到的符号: " + symbol.name);
                    }
                    node.addChild(symbol);
                    break;
                } else {
                    LiteralNonTerminalSymbol nonTerminalTop = (LiteralNonTerminalSymbol) nowSymbol;
                    Production production = table.get(nonTerminalTop.getId()).get(symbol.id);
                    if (production == null) {
                        String message = nonTerminalTop.getMismatchMessage(symbol.id);
                        if (message == null) {
                            stack.pop();
                            node = popIsFinished(node, env);
                            nowSymbol = node.goForward(symbol);
                            continue;
                        }
                        throw node.getMismatchException(message);
                    }
                    node = new ASTNode(production);
                    stack.push(node);
                    nowSymbol = node.goForward(symbol);
                }
            }
            popIsFinished(node, env);
        }

        private ASTNode popIsFinished(ASTNode node, ParsingEnv env) throws SyntaxException {
            while (node.isFinished() && !stack.isEmpty()) {
                NonTerminalSymbol left = node.getLeftSymbol(env);
                stack.pop();
                if (!stack.isEmpty()) {
                    node = stack.peek();
                    node.addChild(left);
                }
            }
            return node;
        }
    }
}
