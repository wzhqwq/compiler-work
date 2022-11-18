package org.wzhqwq.lexical.symbol;

import org.wzhqwq.enums.SymbolIds;

public class NumberSymbol extends TerminalSymbol {
    private final int value;

    public NumberSymbol(int value, int left, int right) {
        super(SymbolIds.NUMBER, "NUMBER", left, right);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
