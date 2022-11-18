package org.wzhqwq.enums;

public class Symbol {
    private final SymbolIds id;
    protected int left;
    protected int right;

    public Symbol(SymbolIds id) {
        this.id = id;
    }

    public SymbolIds getId() {
        return id;
    }
    public int getLeft() {
        return left;
    }
    public int getRight() {
        return right;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
