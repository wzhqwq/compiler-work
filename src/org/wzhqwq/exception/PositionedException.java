package org.wzhqwq.exception;

public class PositionedException extends Exception {
    private final int left;
    private final int right;

    public PositionedException(String message, int left, int right) {
        super(message);
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
