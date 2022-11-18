package org.wzhqwq.lexical.symbol;

import org.wzhqwq.enums.SymbolIds;

public class IdentifierSymbol extends TerminalSymbol {
    private final String identifierName;

    public IdentifierSymbol(String name, int left, int right) {
        super(SymbolIds.IDENTIFIER, "IDENT", left, right);
        this.identifierName = name;
    }

    public String getIdentifierName() {
        return identifierName;
    }
}
