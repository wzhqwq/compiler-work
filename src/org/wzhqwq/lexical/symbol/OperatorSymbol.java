package org.wzhqwq.lexical.symbol;

import org.wzhqwq.enums.OperationTypes;
import org.wzhqwq.enums.SymbolIds;

public class OperatorSymbol extends TerminalSymbol {
    public final OperationTypes variant;

    public OperatorSymbol(SymbolIds id, OperationTypes type, String name, int left, int right) {
        super(id, name, left, right);
        variant = type;
    }
}
