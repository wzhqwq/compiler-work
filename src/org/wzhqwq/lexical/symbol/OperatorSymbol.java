package org.wzhqwq.lexical.symbol;

public class OperatorSymbol extends Symbol {
    private final int priority;

    public OperatorSymbol(int id, String name, int left, int right) {
        super(id, name, left, right);

        switch (SymbolIds.values()[id]) {
            case ASSIGN -> priority = 0;
            case LESS, LESS_EQUAL, GREATER, GREATER_EQUAL, EQUAL, NOT_EQUAL -> priority = 1;
            case PLUS, MINUS -> priority = 2;
            case MULTIPLY, DIVIDE -> priority = 3;
            default -> throw new IllegalStateException("Unexpected value: " + SymbolIds.values()[id]);
        }
    }

    public int getPriority() {
        return priority;
    }
}
