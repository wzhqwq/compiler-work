package org.wzhqwq.lexical.trie;

import org.wzhqwq.lexical.symbol.OperatorSymbol;
import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.enums.OperationTypes;
import org.wzhqwq.enums.SymbolIds;

public class OperatorMatcher {
    private final Trie trie = new Trie();

    public OperatorMatcher() {
        trie.addPattern("+", OperationTypes.PLUS.ordinal());
        trie.addPattern("-", OperationTypes.MINUS.ordinal());

        trie.addPattern("*", OperationTypes.MULTIPLY.ordinal());
        trie.addPattern("/", OperationTypes.DIVIDE.ordinal());

        trie.addPattern("#", OperationTypes.NOT_EQUAL.ordinal());
        trie.addPattern("<", OperationTypes.LESS.ordinal());
        trie.addPattern("<=", OperationTypes.LESS_EQUAL.ordinal());
        trie.addPattern(">", OperationTypes.GREATER.ordinal());
        trie.addPattern(">=", OperationTypes.GREATER_EQUAL.ordinal());

        trie.addPattern("=", SymbolIds.EQUAL.ordinal());
        trie.addPattern(":=", SymbolIds.ASSIGN.ordinal());
    }

    public TerminalSymbol toSym(String operator, int rightPos) {
        try {
            int id = trie.match(operator);
            if (id == SymbolIds.ASSIGN.ordinal() || id == SymbolIds.EQUAL.ordinal()) {
                return new OperatorSymbol(
                        SymbolIds.values()[id],
                        OperationTypes.NONE,
                        operator,
                        rightPos - operator.length() + 1,
                        rightPos
                );
            }
            else {
                return new OperatorSymbol(
                        id < OperationTypes.NOT_EQUAL.ordinal() ? (
                                id < OperationTypes.MULTIPLY.ordinal() ?
                                        SymbolIds.ARITHMETIC_L1 :
                                        SymbolIds.ARITHMETIC_L2
                        ) : SymbolIds.RELATIONAL_PARTIAL,
                        OperationTypes.values()[id],
                        operator,
                        rightPos - operator.length() + 1,
                        rightPos
                );
            }
        }
        catch (Trie.NoMatchedKeywordException ignore) {
            return null;
        }
    }
}
