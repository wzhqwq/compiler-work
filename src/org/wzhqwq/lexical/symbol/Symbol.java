package org.wzhqwq.lexical.symbol;

public class Symbol {
    private final SymbolIds id;
    private final String name;
    private final int left;
    private final int right;

    public Symbol(int id, String name, int left, int right) {
        this.id = SymbolIds.values()[id];
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public String getName() {
        return name;
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
        return name;
    }
}
