package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.OperationTypes;
import org.wzhqwq.enums.SymbolIds;

public class OperationSymbol extends NonTerminalSymbol {
    public OperationTypes variant;

    public OperationSymbol(SymbolIds id) {
        super(id);
    }
}
