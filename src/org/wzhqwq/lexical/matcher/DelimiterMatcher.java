package org.wzhqwq.lexical.matcher;

import org.wzhqwq.lexical.symbol.Symbol;
import org.wzhqwq.lexical.symbol.SymbolIds;
import org.wzhqwq.lexical.utils.Trie;

public class DelimiterMatcher {
    private final Trie trie = new Trie();

    public DelimiterMatcher() {
        trie.addPattern(";", SymbolIds.SEMICOLON.ordinal());
        trie.addPattern(",", SymbolIds.COMMA.ordinal());
        trie.addPattern("(", SymbolIds.BRACKET_LEFT.ordinal());
        trie.addPattern(")", SymbolIds.BRACKET_RIGHT.ordinal());
        trie.addPattern(".", SymbolIds.DOT.ordinal());
    }

    public Symbol toSym(String keyword, int rightPos) {
        try {
            int id = trie.match(keyword);
            return new Symbol(
                    id,
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
