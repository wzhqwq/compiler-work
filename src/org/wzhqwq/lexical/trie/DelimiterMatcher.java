package org.wzhqwq.lexical.trie;

import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.enums.SymbolIds;

public class DelimiterMatcher {
    private final Trie trie = new Trie();

    public DelimiterMatcher() {
        trie.addPattern(";", SymbolIds.SEMICOLON.ordinal());
        trie.addPattern(",", SymbolIds.COMMA.ordinal());
        trie.addPattern("(", SymbolIds.BRACKET_LEFT.ordinal());
        trie.addPattern(")", SymbolIds.BRACKET_RIGHT.ordinal());
        trie.addPattern(".", SymbolIds.DOT.ordinal());
    }

    public TerminalSymbol toSym(String keyword, int rightPos) {
        try {
            int id = trie.match(keyword);
            return new TerminalSymbol(
                    SymbolIds.values()[id],
                    keyword,
                    rightPos,
                    rightPos
            );
        }
        catch (Trie.NoMatchedKeywordException ignore) {
            return null;
        }
    }
}
