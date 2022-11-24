package org.wzhqwq;

import org.wzhqwq.exception.LexicalException;
import org.wzhqwq.exception.PositionedException;
import org.wzhqwq.exception.SyntaxException;
import org.wzhqwq.lexical.LexicalParser;
import org.wzhqwq.enums.Symbol;
import org.wzhqwq.syntax.parser.SyntaxParser;
import org.wzhqwq.util.CodeBuffer;
import org.wzhqwq.vm.VirtualMachine;

import java.util.Scanner;

public class Main {
    private static LexicalParser.ParseResult lexicalResult;
    private static SyntaxParser.ParseResult syntaxResult;
    private static CodeBuffer codeBuffer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder code = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("$")) {
            code.append(line).append('\n');
        }
        codeBuffer = new CodeBuffer(code.toString());

        try {
            GETSYM();
            BLOCK();
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

        run();
    }

    private static void GETSYM() throws LexicalException {
        lexicalResult = new LexicalParser(codeBuffer).parse();
        System.out.println("--------------------词法分析结果--------------------");
        System.out.println("SYM:");
        printTable(lexicalResult.SYM.stream().map(Symbol::toString).toArray(String[]::new));
        System.out.println("\nID:");
        printTable(lexicalResult.ID.toArray(String[]::new));
        System.out.println("\nNUM");
        printTable(lexicalResult.NUM.stream().map(Number::toString).toArray(String[]::new));
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

    private static void BLOCK() throws SyntaxException {
        syntaxResult = new SyntaxParser().parse(lexicalResult.SYM);
        System.out.println("--------------------语法分析结果--------------------");
        System.out.println("TABLE:");
        syntaxResult.table.forEach(System.out::println);
        System.out.println("\nCODE:");
        for (int i = 0; i < syntaxResult.codeList.size(); i++) {
            System.out.println(i + "\t" + syntaxResult.codeList.get(i));
        }
        System.out.println("--------------------------------------------------");
    }

    private static void run() {
        System.out.println("--------------------代码开始运行--------------------");
        VirtualMachine vm = new VirtualMachine(syntaxResult.codeList);
        vm.run();
        System.out.println("--------------------代码结束运行--------------------");
    }
}