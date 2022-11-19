package org.wzhqwq.lexical.symbol;

import org.wzhqwq.enums.Symbol;
import org.wzhqwq.enums.SymbolIds;

public class TerminalSymbol extends Symbol {
    public final String name;
    public final int left;
    public final int right;

    public TerminalSymbol(SymbolIds id, String name, int left, int right) {
        super(id);
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return name;
    }
}
