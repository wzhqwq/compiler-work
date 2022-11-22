package org.wzhqwq.vm;

import org.wzhqwq.syntax.parser.CodeList;

public class Registers {
    public CodeList.Code I; // 指令寄存器
    public int P;           // 程序计数器
    public int T;           // 栈顶寄存器
    public int B;           // 基址寄存器
}
