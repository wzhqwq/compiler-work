package org.wzhqwq.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLine {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Integer> inputs = new ArrayList<>();

    public int read() {
        if (inputs.isEmpty()) {
            String line = "";
            while (line.isEmpty()) {
                System.out.print("> ");
                line = scanner.nextLine();
            }
            for (String str : line.split(" ")) {
                inputs.add(Integer.parseInt(str));
            }
        }
        return inputs.remove(0);
    }
    public void write(int value) {
        System.out.println("< " + value);
    }
}
