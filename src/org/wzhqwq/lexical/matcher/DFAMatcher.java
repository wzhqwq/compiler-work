package org.wzhqwq.lexical.matcher;

import org.wzhqwq.lexical.utils.DFA;

public interface DFAMatcher {
    DFA.Tester getTester();
}
