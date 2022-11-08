package org.wzhqwq.lexical.symbol;

public class IdentifierSymbol extends Symbol {
    private final int index;

    public IdentifierSymbol(int index, int left, int right) {
        super(SymbolIds.IDENTIFIER.ordinal(), "IDENT", left, right);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
