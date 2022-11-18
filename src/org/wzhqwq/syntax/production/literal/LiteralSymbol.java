package org.wzhqwq.syntax.production.literal;

import org.wzhqwq.enums.SymbolIds;

public class LiteralSymbol {
    protected final SymbolIds id;

    public LiteralSymbol(SymbolIds id) {
        this.id = id;
    }

    public SymbolIds getId() {
        return id;
    }
}
