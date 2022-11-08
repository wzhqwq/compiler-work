package org.wzhqwq.lexical.utils;

public class DFA {
    public static class Node {
        // 包含基本ASCII的128个字符，其中NIL(0)代表ε
        public Node[] to = new Node[128];
        public boolean end = false;
    }

    public static class Tester {
        private Node nodeNow;

        Tester(Node start) {
            nodeNow = start;
        }

        public boolean test(char ch) {
            try {
                if (nodeNow.to[ch] == null) {
                    return false;
                }

                nodeNow = nodeNow.to[ch];
                return true;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }

        public boolean isEnd() {
            return nodeNow.end;
        }
    }

    private final Node startNode = new Node();

    public Node getStartNode() {
        return startNode;
    }

    public Tester getTester() {
        return new Tester(startNode);
    }
}
