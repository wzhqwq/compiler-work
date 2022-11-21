package org.wzhqwq.exception;

public class SyntaxException extends PositionedException {
    public final boolean empty;
    public SyntaxException(String message, int left, int right) {
        super("[语法分析] 错误：" + message, left, right);
        empty = message == null;
    }
}
