package org.wzhqwq.syntax.production.literal;

import org.wzhqwq.enums.SymbolIds;

import java.util.Set;

abstract public class LiteralSymbol {
    protected final SymbolIds id;

    public LiteralSymbol(SymbolIds id) {
        this.id = id;
    }

    public SymbolIds getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    abstract public Set<LiteralTerminalSymbol> getFirstSet();
}
