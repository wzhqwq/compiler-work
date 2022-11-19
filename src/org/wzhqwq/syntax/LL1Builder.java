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
        deriveAllFollowSet();
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

        public boolean go(TerminalSymbol symbol, ParsingEnv env) throws SyntaxException {
            if (stack.isEmpty()) {
                if (symbol.id == SymbolIds.EOF) {
                    return true;
                }
                throw new SyntaxException("预期代码已经结束，存在程序外的代码", symbol.left, symbol.right);
            }
            ASTNode node = stack.peek();
            LiteralSymbol nowSymbol = node.goForward(symbol);
            while (true) {
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
                        throw node.getMismatchException(nonTerminalTop.getMismatchMessage(symbol.id));
                    }
                    node = new ASTNode(production);
                    stack.push(node);
                    nowSymbol = node.goForward(symbol);
                }
            }
            while (node.isFinished() && !stack.isEmpty()) {
                NonTerminalSymbol left = node.getLeftSymbol(env);
                node = stack.pop();
                node.addChild(left);
            }

            return false;
        }
    }
}
