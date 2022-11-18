package org.wzhqwq.lexical.symbol;

import org.wzhqwq.enums.Symbol;
import org.wzhqwq.enums.SymbolIds;

public class TerminalSymbol extends Symbol {
    private final String name;

    public TerminalSymbol(SymbolIds id, String name, int left, int right) {
        super(id);
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
