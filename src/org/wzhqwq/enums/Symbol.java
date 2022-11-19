package org.wzhqwq.enums;

public class Symbol {
    public final SymbolIds id;

    public Symbol(SymbolIds id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
