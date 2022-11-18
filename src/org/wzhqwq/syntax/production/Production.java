package org.wzhqwq.syntax.production;

import org.wzhqwq.syntax.production.literal.LiteralNonTerminalSymbol;
import org.wzhqwq.syntax.production.literal.LiteralSymbol;
import org.wzhqwq.syntax.production.literal.LiteralTerminalSymbol;

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
                LiteralSymbol now = right[i];
                if (i == right.length - 1) {
                    ((LiteralNonTerminalSymbol) now).addFollowSetDependency(left);
                }
                else {
                    LiteralSymbol next = right[i + 1];
                    if (next instanceof LiteralTerminalSymbol) {
                        ((LiteralNonTerminalSymbol) now).addFollowSet((LiteralTerminalSymbol) next);
                    } else {
                        Set<LiteralTerminalSymbol> firstSet = ((LiteralNonTerminalSymbol) next).getFirstSet();
                        ((LiteralNonTerminalSymbol) now).addFollowSet(firstSet);
                        if (firstSet.contains(LiteralTerminalSymbol.EPSILON)) {
                            ((LiteralNonTerminalSymbol) now).addFollowSetDependency(left);
                        }
                    }
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
        Set<LiteralTerminalSymbol> firstSet = new HashSet<>();
        for (LiteralSymbol symbol : right) {
            if (symbol instanceof LiteralTerminalSymbol) {
                firstSet.add((LiteralTerminalSymbol) symbol);
                // 到这里结束了，ε也就不会出现在first集中了
                firstSet.remove(LiteralTerminalSymbol.EPSILON);
                break;
            }
            else {
                Set<LiteralTerminalSymbol> t = ((LiteralNonTerminalSymbol) symbol).getFirstSet();
                firstSet.addAll(t);
                if (!t.contains(LiteralTerminalSymbol.EPSILON)) {
                    // 到这里结束了，ε也就不会出现在first集中了
                    firstSet.remove(LiteralTerminalSymbol.EPSILON);
                    break;
                }
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
}
