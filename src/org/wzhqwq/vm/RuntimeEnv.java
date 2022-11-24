package org.wzhqwq.vm;

import org.wzhqwq.syntax.parser.CodeList;

import java.util.ArrayList;
import java.util.List;

public class RuntimeEnv {
    public final Registers registers = new Registers();
    public final List<CodeList.Code> instructions;
    public final List<Integer> stack = new ArrayList<>();

    private boolean finished = false;

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

        // 按级差沿着静态链找到变量所在过程的基地址
        for (int i = 0; i < level; i++) {
            registers.B = getStaticLink();
        }
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
    public int pop() {
        int num = stack.remove(registers.T);
        registers.T = stack.size() - 1;
        return num;
    }
    public void jump(int address) {
        registers.P = address;
    }

    public void call(int level, int address) {
        // 存SL，如果级差是0，说明是调用子过程，静态链指向自身基址；
        // 否则是调用外层过程的子过程（包含调用自身），需要沿着静态链找到外层过程的基址
        int originalBase = registers.B;
        // 按级差沿着静态链找到被调用过程所在过程的基地址
        for (int i = 0; i < level; i++) {
            registers.B = getStaticLink();
        }
        stack.add(registers.B);
        registers.B = originalBase;
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
        if (registers.B == 0) {
            finished = true;
            return;
        }
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

    public boolean isFinished() {
        return finished;
    }
}
