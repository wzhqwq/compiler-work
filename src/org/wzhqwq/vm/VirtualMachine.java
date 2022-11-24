package org.wzhqwq.vm;

import org.wzhqwq.enums.OperationTypes;
import org.wzhqwq.syntax.parser.CodeList;

import java.util.List;

public class VirtualMachine {
    private final RuntimeEnv env;
    private final CommandLine commandLine = new CommandLine();

    public VirtualMachine(List<CodeList.Code> instructions) {
        this.env = new RuntimeEnv(instructions);
    }

    private void execute(CodeList.Code instruction) {
        switch (instruction.instruction) {
            case LIT -> env.push(instruction.address);
            case LOD -> env.push(env.stack.get(env.getVariableAddress(instruction.level, instruction.address)));
            case STO -> env.stack.set(env.getVariableAddress(instruction.level, instruction.address), env.pop());
            case CAL -> env.call(instruction.level, instruction.address);
            case INT -> env.staticAllocate(instruction.address);
            case JMP -> env.jump(instruction.address);
            case JPC -> {
                if (env.pop() == 0) env.jump(instruction.address);
            }
            case OPR -> doOperation(OperationTypes.values()[instruction.address]);
        }
    }
    private void doOperation(OperationTypes opr) {
        switch (opr) {
            case NONE -> env.pop();
            case EXIT -> env.returnFromCall();
            case PLUS -> env.push(env.pop() + env.pop());
            case MINUS -> env.push(-env.pop() + env.pop());
            case MULTIPLY -> env.push(env.pop() * env.pop());
            case DIVIDE -> {
                int divisor = env.pop();
                env.push(env.pop() / divisor);
            }
            case EQUAL -> env.push(env.pop() == env.pop() ? 1 : 0);
            case NOT_EQUAL -> env.push(env.pop() != env.pop() ? 1 : 0);
            case LESS -> env.push(env.pop() > env.pop() ? 1 : 0);
            case LESS_EQUAL -> env.push(env.pop() >= env.pop() ? 1 : 0);
            case GREATER -> env.push(env.pop() < env.pop() ? 1 : 0);
            case GREATER_EQUAL -> env.push(env.pop() <= env.pop() ? 1 : 0);
            case ODD -> env.push(env.pop() % 2 == 1 ? 1 : 0);
            case WRITE -> commandLine.write(env.pop());
            case READ -> env.push(commandLine.read());
        }
    }

    private void tick() {
        env.readInstruction();
        execute(env.registers.I);
    }

    public void run() {
        while (!env.isFinished()) tick();
    }
}
