package org.wzhqwq.lexical.matcher;

import org.wzhqwq.lexical.symbol.OperatorSymbol;
import org.wzhqwq.lexical.symbol.Symbol;
import org.wzhqwq.lexical.symbol.SymbolIds;
import org.wzhqwq.lexical.utils.Trie;

public class OperatorMatcher {
    private final Trie trie = new Trie();

    public OperatorMatcher() {
        trie.addPattern("+", SymbolIds.PLUS.ordinal());
        trie.addPattern("-", SymbolIds.MINUS.ordinal());
        trie.addPattern("*", SymbolIds.MULTIPLY.ordinal());
        trie.addPattern("/", SymbolIds.DIVIDE.ordinal());
        trie.addPattern("=", SymbolIds.EQUAL.ordinal());
        trie.addPattern("#", SymbolIds.NOT_EQUAL.ordinal());
        trie.addPattern("<", SymbolIds.LESS.ordinal());
        trie.addPattern("<=", SymbolIds.LESS_EQUAL.ordinal());
        trie.addPattern(">", SymbolIds.GREATER.ordinal());
        trie.addPattern(">=", SymbolIds.GREATER_EQUAL.ordinal());
        trie.addPattern(":=", SymbolIds.ASSIGN.ordinal());
    }

    public Symbol toSym(String keyword, int rightPos) {
        try {
            int id = trie.match(keyword);
            return new OperatorSymbol(
                    id,
                    keyword,
                    rightPos - keyword.length() + 1,
                    rightPos
            );
        }
        catch (Trie.NoMatchedKeywordException ignore) {
            return null;
        }
    }
}
