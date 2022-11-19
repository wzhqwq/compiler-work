package org.wzhqwq.syntax.production;

import org.wzhqwq.syntax.production.literal.LiteralNonTerminalSymbol;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.literal.LiteralTerminalSymbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Production {
    private final LiteralNonTerminalSymbol left;
    private final LiteralSymbol[] right;
    private Set<LiteralTerminalSymbol> selectSet = null;
    public final ProductionProcess process;

    public Production(LiteralNonTerminalSymbol left, LiteralSymbol[] right, ProductionProcess process) {
        this.left = left;
        this.right = right;
        this.process = process;
    }
    public Production(LiteralNonTerminalSymbol left, LiteralSymbol[] right) {
        this(left, right, null);
    }

    public void deriveFollowSetsOfRight() {
        for (int i = 0; i < right.length; i++) {
            if (right[i] instanceof LiteralNonTerminalSymbol) {
                LiteralNonTerminalSymbol now = (LiteralNonTerminalSymbol) right[i];
                if (i == right.length - 1) {
                    now.addFollowSetDependency(left);
                }
                else {
                    Set<LiteralTerminalSymbol> firstSetOfNext = new HashSet<>(getFirstSetOfSuffix(i + 1));
                    if (firstSetOfNext.contains(LiteralTerminalSymbol.EPSILON)) {
                        firstSetOfNext.remove(LiteralTerminalSymbol.EPSILON);
                        now.addFollowSetDependency(left);
                    }
                    now.addFollowSet(firstSetOfNext);
                }
            }
        }
    }

    public LiteralNonTerminalSymbol getLeft() {
        return left;
    }
    public LiteralSymbol[] getRight() {
        return right;
    }
    public Set<LiteralTerminalSymbol> getFirstSetOfRight() {
        return getFirstSetOfSuffix(0);
    }
    private Set<LiteralTerminalSymbol> getFirstSetOfSuffix(int start) {
        Set<LiteralTerminalSymbol> firstSet = new HashSet<>();
        for (int i = start; i < right.length; i++) {
            Set<LiteralTerminalSymbol> symbolFirstSet = right[i].getFirstSet();
            firstSet.addAll(symbolFirstSet);
            if (!symbolFirstSet.contains(LiteralTerminalSymbol.EPSILON)) {
                // 到这里结束了，ε也就不会出现在first集中了
                firstSet.remove(LiteralTerminalSymbol.EPSILON);
                break;
            }
        }
        return firstSet;
    }
    public Set<LiteralTerminalSymbol> getSelectSet() {
        // 懒加载
        if (selectSet == null) {
            selectSet = new HashSet<>();
            selectSet.addAll(getFirstSetOfRight());
            if (selectSet.contains(LiteralTerminalSymbol.EPSILON)) {
                selectSet.remove(LiteralTerminalSymbol.EPSILON);
                selectSet.addAll(left.getFollowSet());
            }
        }
        return selectSet;
    }

    @Override
    public String toString() {
        return left + " -> " + String.join(" ", Arrays.stream(right)
                .map(LiteralSymbol::toString)
                .toArray(String[]::new)
        );
    }
}
