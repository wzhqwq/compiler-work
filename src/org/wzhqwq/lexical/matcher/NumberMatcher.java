package org.wzhqwq.lexical.matcher;

import org.wzhqwq.lexical.utils.DFA;

public class NumberMatcher implements DFAMatcher {
    private final DFA.Tester tester;

    @Override
    public DFA.Tester getTester() {
        return tester;
    }

    public NumberMatcher() {
        DFA dfa = new DFA();
        DFA.Node startNode = dfa.getStartNode();
        DFA.Node endNode = new DFA.Node();
        endNode.end = true;
        for (char c = '0'; c <= '9'; c++) {
            startNode.to[c] = endNode;
            endNode.to[c] = endNode;
        }

        tester = dfa.getTester();
    }
}
