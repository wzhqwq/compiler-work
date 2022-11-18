package org.wzhqwq.syntax.production.literal;

import org.wzhqwq.enums.SymbolIds;
import org.wzhqwq.syntax.production.Production;
import org.wzhqwq.syntax.production.ProductionProcess;
import org.wzhqwq.syntax.symbol.NonTerminalSymbol;

import java.util.*;

public class LiteralNonTerminalSymbol extends LiteralSymbol {
    private Set<LiteralTerminalSymbol> firstSet = null;
    private final Set<LiteralTerminalSymbol> followSet = new HashSet<>();
    private final List<Production> productions = new ArrayList<>();
    private final Set<LiteralNonTerminalSymbol> followSetDependencies = new HashSet<>();

    public LiteralNonTerminalSymbol(SymbolIds id) {
        super(id);
    }

    public Production addProduction(LiteralSymbol[] right, ProductionProcess processes) {
        Production production = new Production(this, right, processes);
        production.deriveFollowSetsOfRight();
        productions.add(production);
        return production;
    }
    public Production addProduction(LiteralSymbol[] right) {
        return addProduction(right, null);
    }
    public List<Production> getProductions() {
        return productions;
    }

    public Set<LiteralTerminalSymbol> getFirstSet() {
        if (firstSet == null) {
            firstSet = new HashSet<>();
            for (Production production : productions) {
                firstSet.addAll(production.getFirstSetOfRight());
            }
        }
        return firstSet;
    }
    public Set<LiteralTerminalSymbol> getFollowSet() {
        return followSet;
    }
    // 对于S->...A，需要添加A的follow集，考虑到循环依赖，需要统一计算
    public void addFollowSetDependency(LiteralNonTerminalSymbol symbol) {
        followSetDependencies.add(symbol);
    }
    // 对于S->...Aβ...，需要添加β
    public void addFollowSet(LiteralTerminalSymbol symbol) {
        followSet.add(symbol);
    }
    // 对于S->...AB...，需要添加B的first集
    public void addFollowSet(Set<LiteralTerminalSymbol> symbols) {
        followSet.addAll(symbols);
    }
    // 利用循环依赖更新一次follow集
    public boolean updateFollowSetOnce() {
        int setSize = followSet.size();
        for (LiteralNonTerminalSymbol symbol : followSetDependencies) {
            addFollowSet(symbol.getFollowSet());
        }
        return setSize != followSet.size();
    }

    public NonTerminalSymbol toNonTerminalSymbol() {
        return new NonTerminalSymbol(id);
    }
    public String getMismatchMessage(SymbolIds symbolId) {
        return null;
    }
}
