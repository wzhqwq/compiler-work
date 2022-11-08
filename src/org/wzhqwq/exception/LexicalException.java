package org.wzhqwq.exception;

public class LexicalException extends PositionedException {
    public LexicalException(String message, int left, int right) {
        super("[词法分析] 错误：" + message, left, right);
    }
}
