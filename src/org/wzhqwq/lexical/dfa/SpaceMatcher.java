package org.wzhqwq.lexical.dfa;

public class SpaceMatcher implements DFAMatcher {
    private final DFA.Tester tester;

    @Override
    public DFA.Tester getTester() {
        return tester;
    }

    public SpaceMatcher() {
        DFA dfa = new DFA();
        DFA.Node startNode = dfa.getStartNode();
        startNode.end = true;

        startNode.to[' '] = startNode.to['\t'] = startNode.to['\n'] = startNode.to['\r'] = startNode;

        tester = dfa.getTester();
    }
}
