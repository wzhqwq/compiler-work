package org.wzhqwq.syntax.production.literal;

import org.wzhqwq.enums.SymbolIds;

public class LiteralTerminalSymbol extends LiteralSymbol {
    public static final LiteralTerminalSymbol EPSILON = new LiteralTerminalSymbol(SymbolIds.EPSILON);

    public LiteralTerminalSymbol(SymbolIds id) {
        super(id);
    }
}
