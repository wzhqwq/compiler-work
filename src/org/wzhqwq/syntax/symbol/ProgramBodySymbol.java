package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class ProgramBodySymbol extends NonTerminalSymbol {
    public int entryPoint;

    public ProgramBodySymbol() {
        super(SymbolIds.PROGRAM_BODY);
    }
}
