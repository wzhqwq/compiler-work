package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class MSymbol extends NonTerminalSymbol {
    public int nextPtr;

    public MSymbol() {
        super(SymbolIds.M);
    }
}
