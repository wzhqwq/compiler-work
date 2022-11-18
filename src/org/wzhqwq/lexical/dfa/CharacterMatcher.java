package org.wzhqwq.lexical.dfa;

public class CharacterMatcher implements DFAMatcher {
    private final DFA.Tester tester;

    @Override
    public DFA.Tester getTester() {
        return tester;
    }

    public CharacterMatcher() {
        DFA dfa = new DFA();
        DFA.Node startNode = dfa.getStartNode();

        // 匹配单字符运算符
        DFA.Node oneLetterNode = new DFA.Node();
        oneLetterNode.end = true;

        char[] singleLetterOperators = {'+', '-', '*', '/', '=', '#', '(', ')', ';', ',', '.'};
        for (char ch : singleLetterOperators) {
            startNode.to[ch] = oneLetterNode;
        }

        // 匹配赋值运算符
        DFA.Node assignmentFirstLetterNode = new DFA.Node();
        DFA.Node assignmentSecondLetterNode = new DFA.Node();
        assignmentSecondLetterNode.end = true;

        startNode.to[':'] = assignmentFirstLetterNode;
        assignmentFirstLetterNode.to['='] = assignmentSecondLetterNode;

        // 匹配<, <=, >, >=
        DFA.Node oneLetterCompareNode = new DFA.Node();
        oneLetterCompareNode.end = true;
        DFA.Node twoLetterCompareNode = new DFA.Node();
        twoLetterCompareNode.end = true;

        startNode.to['<'] = oneLetterCompareNode;
        startNode.to['>'] = oneLetterCompareNode;
        oneLetterCompareNode.to['='] = twoLetterCompareNode;

        tester = dfa.getTester();
    }
}
