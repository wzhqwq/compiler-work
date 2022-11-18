package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class StatementSymbol extends NonTerminalSymbol {
    public int[] nextList;

    public StatementSymbol(SymbolIds id) {
        super(id);
    }
}
