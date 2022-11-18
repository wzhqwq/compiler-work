package org.wzhqwq.syntax.parser;

import java.util.Map;

public class Table {
    public static class Row {
        final String name;
        final TableRowType type;

        public Row(String name, TableRowType type) {
            this.name = name;
            this.type = type;
        }
    }
    public static class ConstantRow extends Row {
        final int value;

        public ConstantRow(String name, int value) {
            super(name, TableRowType.CONSTANT);
            this.value = value;
        }
    }
    public static class VariableRow extends Row {
        final int level;
        final int address;

        public VariableRow(String name, int level, int address) {
            super(name, TableRowType.VARIABLE);
            this.level = level;
            this.address = address;
        }
    }
    public static class ProcedureRow extends Row {
        final int level;
        final int address;

        public ProcedureRow(String name, int level, int address) {
            super(name, TableRowType.PROCEDURE);
            this.level = level;
            this.address = address;
        }
    }

    private final Map<String, Row> rows = new java.util.HashMap<>();
    private int level = 0;
    private int address = 0;

    public void addConstant(String name, int value) {
        rows.put(name, new ConstantRow(name, value));
    }
    public void addVariable(String name) {
        rows.put(name, new VariableRow(name, level, address++));
    }
    public void enterProcedure(String name, int startAddress) {
        rows.put(name, new ProcedureRow(name, level, startAddress));
        level++;
        address = 3;
    }
    public void leaveProcedure() {
        level--;
    }
    public Row findRow(String name) {
        return rows.get(name);
    }
    public int getLevel() {
        return level;
    }
}
