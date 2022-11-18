package org.wzhqwq.exception;

public class SyntaxException extends PositionedException {
    public SyntaxException(String message, int left, int right) {
        super("[语法分析] 错误：" + message, left, right);
    }
}
