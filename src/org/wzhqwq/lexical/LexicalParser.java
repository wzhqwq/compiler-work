package org.wzhqwq.lexical;

import org.wzhqwq.exception.LexicalException;
import org.wzhqwq.lexical.matcher.*;
import org.wzhqwq.lexical.symbol.IdentifierSymbol;
import org.wzhqwq.lexical.symbol.NumberSymbol;
import org.wzhqwq.lexical.symbol.Symbol;
import org.wzhqwq.util.CodeBuffer;

import java.util.ArrayList;
import java.util.List;

public class LexicalParser {
    private final CodeBuffer buffer;
    private final KeywordMatcher keywordMatcher = new KeywordMatcher();

    private final ArrayList<Integer> numbers = new ArrayList<>();
    private final ArrayList<String> identifiers = new ArrayList<>();

    public LexicalParser(CodeBuffer codeBuffer) {
        buffer = codeBuffer;
    }

    public static class ParseResult {
        public final Symbol[] SYM;
        public final String[] ID;
        public final Number[] NUM;

        public ParseResult(Symbol[] SYM, String[] ID, Number[] NUM) {
            this.SYM = SYM;
            this.ID = ID;
            this.NUM = NUM;
        }
    }

    private Symbol round() throws LexicalException {
        StringBuilder batch = new StringBuilder();
        List<DFAMatcher> possibleMatcher = new ArrayList<>(List.of(new DFAMatcher[]{
                new IdentifierMatcher(),
                new NumberMatcher(),
                new CharacterMatcher()
        }));

        while (buffer.peekChar() != '\0' && possibleMatcher.size() > 1) {
            possibleMatcher.removeIf(matcher -> !matcher.getTester().test(buffer.peekChar()) && !matcher.getTester().isEnd());
            batch.append(buffer.readChar());
        }
        if (possibleMatcher.size() != 1) {
            throw new LexicalException("无法识别的词法：" + batch, buffer.getPosition() - batch.length() + 1, buffer.getPosition());
        }

        DFAMatcher matched = possibleMatcher.get(0);
        while (buffer.peekChar() != '\0' && matched.getTester().test(buffer.peekChar())) {
            batch.append(buffer.readChar());
        }
        if (matched instanceof IdentifierMatcher) {
            Symbol keywordSymbol = keywordMatcher.toSym(batch.toString(), buffer.getPosition());
            if (keywordSymbol != null) {
                return keywordSymbol;
            }
            identifiers.add(batch.toString());
            return new IdentifierSymbol(identifiers.size() - 1, buffer.getPosition() - batch.length() + 1, buffer.getPosition());
        } else if (matched instanceof NumberMatcher) {
            int number = Integer.parseInt(batch.toString());
            numbers.add(number);
            return new NumberSymbol(number, buffer.getPosition() - batch.length() + 1, buffer.getPosition());
        } else if (matched instanceof CharacterMatcher) {
            Symbol result = (new OperatorMatcher()).toSym(batch.toString(), buffer.getPosition());
            if (result != null) {
                return result;
            }
            return (new DelimiterMatcher()).toSym(batch.toString(), buffer.getPosition());
        }
        throw new LexicalException("无法识别的词法：" + batch, buffer.getPosition() - batch.length() + 1, buffer.getPosition());
    }

    private void trimSpace() {
        SpaceMatcher matcher = new SpaceMatcher();
        while (matcher.getTester().test(buffer.peekChar())) {
            buffer.readChar();
        }
    }

    public ParseResult parse() throws LexicalException {
        numbers.clear();
        identifiers.clear();

        ArrayList<Symbol> symbols = new ArrayList<>();

        trimSpace();

        while (buffer.peekChar() != '\0') {
            symbols.add(round());
            trimSpace();
        }
        return new ParseResult(
                symbols.toArray(new Symbol[0]),
                identifiers.toArray(new String[0]),
                numbers.toArray(new Integer[0])
        );
    }
}

