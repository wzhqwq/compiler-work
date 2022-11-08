package org.wzhqwq.lexical.utils;

public class Trie {
    public static class Node {
        Node[] to = new Node[128];
        int end = -1;
    }

    public static class NoMatchedKeywordException extends Exception {
        public NoMatchedKeywordException() {
            super("未找到匹配的关键字");
        }
    }

    private final Node root = new Node();

    public void addPattern(String pattern, int end) {
        Node p = root;
        for (int i = 0; i < pattern.length(); i++) {
            int c = pattern.charAt(i);
            if (p.to[c] == null) {
                p.to[c] = new Node();
            }
            p = p.to[c];
        }
        p.end = end;
    }

    public int match(String pattern) throws NoMatchedKeywordException {
        Node p = root;
        for (int i = 0; i < pattern.length(); i++) {
            int c = pattern.charAt(i);
            if (p.to[c] == null) {
                throw new NoMatchedKeywordException();
            }
            p = p.to[c];
        }
        if (p.end < 0) {
            throw new NoMatchedKeywordException();
        }
        return p.end;
    }
}
