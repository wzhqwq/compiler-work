package org.wzhqwq.syntax.symbol;

import org.wzhqwq.enums.SymbolIds;

public class ConditionSymbol extends NonTerminalSymbol {
    public int[] trueList;
    public int[] falseList;

    public ConditionSymbol() {
        super(SymbolIds.CONDITION);
    }
}
