package org.wzhqwq.lexical.trie;

import org.wzhqwq.lexical.symbol.TerminalSymbol;
import org.wzhqwq.enums.SymbolIds;

// program，const，var，procedure，begin，end，if，then，else，call，while，do，read，write
public class KeywordMatcher {
    private final Trie trie = new Trie();
    private final String[] symbols = {
        "CONSTSYM",
        "VARSYM",
        "PROCSYM",
        "BEGINSYM",
        "ENDSYM",
        "IFSYM",
        "THENSYM",
        "ODDSYM",
        "CALLSYM",
        "WHILESYM",
        "DOSYM",
        "READSYM",
        "WRITESYM"
    };

    public KeywordMatcher() {
        trie.addPattern("const", SymbolIds.CONST_KEYWORD.ordinal());
        trie.addPattern("var", SymbolIds.VAR_KEYWORD.ordinal());
        trie.addPattern("procedure", SymbolIds.PROCEDURE_KEYWORD.ordinal());
        trie.addPattern("begin", SymbolIds.BEGIN_KEYWORD.ordinal());
        trie.addPattern("end", SymbolIds.END_KEYWORD.ordinal());
        trie.addPattern("if", SymbolIds.IF_KEYWORD.ordinal());
        trie.addPattern("then", SymbolIds.THEN_KEYWORD.ordinal());
        trie.addPattern("odd", SymbolIds.ODD_KEYWORD.ordinal());
        trie.addPattern("call", SymbolIds.CALL_KEYWORD.ordinal());
        trie.addPattern("while", SymbolIds.WHILE_KEYWORD.ordinal());
        trie.addPattern("do", SymbolIds.DO_KEYWORD.ordinal());
        trie.addPattern("read", SymbolIds.READ_KEYWORD.ordinal());
        trie.addPattern("write", SymbolIds.WRITE_KEYWORD.ordinal());
    }

    public TerminalSymbol toSym(String keyword, int rightPos) {
        try {
            int id = trie.match(keyword);
            return new TerminalSymbol(
                SymbolIds.values()[id],
                symbols[id - SymbolIds.CONST_KEYWORD.ordinal()],
                rightPos - keyword.length() + 1,
                rightPos
            );
        }
        catch (Trie.NoMatchedKeywordException ignore) {
            return null;
        }
    }
}
