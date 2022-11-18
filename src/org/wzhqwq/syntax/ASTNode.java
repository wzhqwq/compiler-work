package org.wzhqwq.syntax;

import org.wzhqwq.enums.Symbol;
import org.wzhqwq.exception.SyntaxException;
import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.syntax.parser.ParsingEnv;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.Production;
import org.wzhqwq.syntax.symbol.NonTerminalSymbol;

public class ASTNode {
    private final Production production;
    private final Symbol[] children;
    private int index = 0;
    private int left, right;
    public ASTNode(Production production) {
        this.production = production;
        children = new Symbol[production.getRight().length];
    }

    public LiteralSymbol goForward(TerminalSymbol symbol) {
        if (index == 0) {
            left = symbol.getLeft();
        }
        right = symbol.getRight();
        return production.getRight()[index++];
    }

    public void addChild(Symbol symbol) {
        children[index - 1] = symbol;
    }
    public NonTerminalSymbol getLeftSymbol(ParsingEnv env) throws SyntaxException {
        NonTerminalSymbol leftSymbol = production.getLeft().toNonTerminalSymbol();
        if (production.process != null) production.process.process(leftSymbol, children, env);
        return leftSymbol;
    }

    public boolean isFinished() {
        return index == production.getRight().length;
    }

    public SyntaxException getMismatchException(String message) {
        return new SyntaxException(message, left, right);
    }
}
