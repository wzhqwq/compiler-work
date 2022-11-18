package org.wzhqwq.syntax.parser;

import org.wzhqwq.enums.Instructions;

import java.util.List;

public class CodeList {
    public static class Code {
        Instructions instruction;
        int level;
        int address;

        public Code(Instructions instruction, int level, int address) {
            this.instruction = instruction;
            this.level = level;
            this.address = address;
        }
    }

    private List<Code> codeList;

    public void pushCode(Instructions instruction, int level, int address) {
        codeList.add(new Code(instruction, level, address));

    }
    public int[] pushCode(Instructions instruction, int level) {
        codeList.add(new Code(instruction, level, 0));
        return new int[]{ getCodePtr() - 1 };
    }
    public int getCodePtr() {
        return codeList.size();
    }
    public void fill(int[] ptrList, int address) {
        for (int ptr : ptrList) {
            codeList.get(ptr).address = address;
        }
    }
    public int[] merge(int[] ptrList1, int[] ptrList2) {
        int[] ptrList = new int[ptrList1.length + ptrList2.length];
        System.arraycopy(ptrList1, 0, ptrList, 0, ptrList1.length);
        System.arraycopy(ptrList2, 0, ptrList, ptrList1.length, ptrList2.length);
        return ptrList;
    }
}
