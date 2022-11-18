package org.wzhqwq.syntax.parser;

import org.wzhqwq.lexical.symbol.IdentifierSymbol;

import java.util.ArrayList;
import java.util.List;

public class ParsingEnv {
    public final Table table;
    public final CodeList codeList;
    public final List<IdentifierSymbol> identifiers = new ArrayList<>();

    public ParsingEnv() {
        table = new Table();
        codeList = new CodeList();
    }
}
