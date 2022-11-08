package org.wzhqwq.lexical.matcher;

import org.wzhqwq.lexical.utils.DFA;

public class IdentifierMatcher implements DFAMatcher {
    private final DFA.Tester tester;

    @Override
    public DFA.Tester getTester() {
        return tester;
    }

    public IdentifierMatcher() {
        DFA dfa = new DFA();
        DFA.Node startNode = dfa.getStartNode();
        DFA.Node endNode = new DFA.Node();
        endNode.end = true;
        // 字母开头
        for (char c = 'a'; c <= 'z'; c++) {
            startNode.to[c] = endNode;
            endNode.to[c] = endNode;
        }
        // 后面跟字母数字
        for (char c = '0'; c <= '9'; c++) {
            endNode.to[c] = endNode;
        }

        tester = dfa.getTester();
    }
}
