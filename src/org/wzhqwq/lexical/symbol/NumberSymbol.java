package org.wzhqwq.lexical.symbol;

public class NumberSymbol extends Symbol {
    private final int value;

    public NumberSymbol(int value, int left, int right) {
        super(SymbolIds.NUMBER.ordinal(), "NUMBER", left, right);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
