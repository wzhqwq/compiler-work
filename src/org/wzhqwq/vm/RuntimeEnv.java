package org.wzhqwq.vm;

import org.wzhqwq.syntax.parser.CodeList;

import java.util.ArrayList;
import java.util.List;

public class RuntimeEnv {
    public final Registers registers = new Registers();
    public final List<CodeList.Code> instructions;
    public final List<Integer> stack = new ArrayList<>();

    public RuntimeEnv(List<CodeList.Code> instructions) {
        this.instructions = instructions;
    }

    // SL静态链
    public int getStaticLink() {
        return stack.get(registers.B);
    }

    // DL动态链
    public int getDynamicLink() {
        return stack.get(registers.B + 1);
    }

    // RA返回地址
    public int getReturnAddress() {
        return stack.get(registers.B + 2);
    }

    public int getVariableAddress(int level, int address) {
        int originalBase = registers.B;

        for (int i = 0; i < level; i++) {
            registers.B = getStaticLink();
        }
        if (registers.B == 0) address += 3;
        address = registers.B + address;

        registers.B = originalBase;
        return address;
    }

    // 静态分配
    public void staticAllocate(int size) {
        // call过程已经为SL、DL、RA分配了空间
        if (registers.B != 0) size -= 3;
        for (int i = 0; i < size; i++) {
            stack.add(0);
        }
        registers.T = stack.size() - 1;
    }

    // 动态分配
    public void push(int value) {
        stack.add(value);
        registers.T = stack.size() - 1;
    }
    public void pop() {
        stack.remove(registers.T);
        registers.T = stack.size() - 1;
    }
    public int top() {
        return stack.get(registers.T);
    }
    public int secondTop() {
        return stack.get(registers.T - 1);
    }

    public void jump(int address) {
        registers.P = address;
    }

    public void call(int address) {
        // 存SL

        // 存DL
        stack.add(registers.B);
        // 存RA
        stack.add(registers.P);
        // 更新基址和栈顶
        registers.B = registers.T + 1;
        registers.T = stack.size() - 1;
        // 跳转
        jump(address);
    }

    public void returnFromCall() {
        int originalBase = registers.B;
        // 回到断点，恢复基址
        registers.P = getReturnAddress();
        registers.B = getDynamicLink();
        // 释放空间
        stack.subList(originalBase, stack.size()).clear();
        // 更新栈顶
        registers.T = stack.size() - 1;
    }

    public void readInstruction() {
        registers.I = instructions.get(registers.P);
        registers.P++;
    }
}
