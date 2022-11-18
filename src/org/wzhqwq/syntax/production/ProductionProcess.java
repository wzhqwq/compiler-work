package org.wzhqwq.syntax.production;

import org.wzhqwq.enums.Symbol;
import org.wzhqwq.exception.SyntaxException;
import org.wzhqwq.syntax.parser.ParsingEnv;
import org.wzhqwq.syntax.symbol.NonTerminalSymbol;

public interface ProductionProcess {
    void process(NonTerminalSymbol left, Symbol[] right, ParsingEnv env) throws SyntaxException;
}
