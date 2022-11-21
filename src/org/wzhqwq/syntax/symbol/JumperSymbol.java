package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class JumperSymbol extends NonTerminalSymbol {
    public int[] jumpList;
    public JumperSymbol() {
        super(SymbolIds.JUMPER);
    }
}
