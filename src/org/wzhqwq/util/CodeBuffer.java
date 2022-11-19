package org.wzhqwq.util;

public class CodeBuffer {
    private final String code;
    private int p = 0;
    public CodeBuffer(String code) {
        this.code = code;
    }

    public char readChar() {
        return p >= code.length() ? '\0' : code.charAt(p++);
    }

    public char peekChar() {
        return p >= code.length() ? '\0' : code.charAt(p);
    }

    public int getPosition() {
        return p - 1;
    }

    public String getPositionMessage(int l, int r) {
        int line = 1;
        int column = 1;
        for (int i = 0; i < l; i++) {
            if (code.charAt(i) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return code.split("\n")[line - 1] + "\n" +
                " ".repeat(Math.max(0, column - 1)) + "~".repeat(Math.max(0, r - l + 1)) + '\n' +
                "在第" + line + "行第" + column + "列" + '\n';
    }
}
