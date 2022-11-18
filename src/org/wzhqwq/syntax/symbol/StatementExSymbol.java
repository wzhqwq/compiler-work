package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class StatementExSymbol extends StatementSymbol {
    public int nextPtr;

    public StatementExSymbol(SymbolIds id) {
        super(id);
    }
}
