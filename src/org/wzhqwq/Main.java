package org.wzhqwq;

import org.wzhqwq.exception.LexicalException;
import org.wzhqwq.exception.PositionedException;
import org.wzhqwq.lexical.LexicalParser;
import org.wzhqwq.enums.Symbol;
import org.wzhqwq.util.CodeBuffer;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static LexicalParser.ParseResult lexicalResult;
    private static CodeBuffer codeBuffer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder code = new StringBuilder();
        while (scanner.hasNext()) {
            code.append(scanner.nextLine()).append('\n');
        }
        codeBuffer = new CodeBuffer(code.toString());

        try {
            GETSYM();
        }
        catch (Exception e) {
            System.out.println("编译失败：");
            System.out.println(e.getMessage());
            if (e instanceof PositionedException) {
                PositionedException exception = (PositionedException) e;
                System.out.println(codeBuffer.getPositionMessage(exception.getLeft(), exception.getRight()));
            }
            e.printStackTrace();
        }
    }

    private static void GETSYM() throws LexicalException {
        lexicalResult = new LexicalParser(codeBuffer).parse();
        System.out.println("--------------------词法分析结果--------------------");
        System.out.println("SYM:");
        printTable(Arrays.stream(lexicalResult.SYM).map(Symbol::toString).toArray(String[]::new));
        System.out.println("\nID:");
        printTable(lexicalResult.ID);
        System.out.println("\nNUM");
        printTable(Arrays.stream(lexicalResult.NUM).map(Number::toString).toArray(String[]::new));
        System.out.println("--------------------------------------------------");
    }

    private static void printTable(String[] arr) {
        for (int line = 0; line <= arr.length / 15; line++) {
            for (int i = 0; i < 15; i++) {
                int index = line * 15 + i;
                if (index >= arr.length) {
                    break;
                }
                System.out.print(arr[index]);
                for (int j = (11 - arr[index].length()) / 4; j >= 0; j--) {
                    System.out.print('\t');
                }
            }
            if (line * 15 < arr.length) {
                System.out.println();
            }
        }
    }
}