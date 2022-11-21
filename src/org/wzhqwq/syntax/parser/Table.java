package org.wzhqwq.syntax.parser;

import java.util.List;
import java.util.Map;

public class Table {
    public static class Row {
        final String name;
        final TableRowType type;

        public Row(String name, TableRowType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("Name: %-5s | Kind: %-10s", name, type);
        }
    }
    public static class ConstantRow extends Row {
        final int value;

        public ConstantRow(String name, int value) {
            super(name, TableRowType.CONSTANT);
            this.value = value;
        }

        @Override
        public String toString() {
            return super.toString() + String.format(" | Value: %d", value);
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

        @Override
        public String toString() {
            return super.toString() +
                    String.format(" | Level: %-5d | Address: DX + %d", level, address);
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

        @Override
        public String toString() {
            return super.toString() +
                    String.format(" | Level: %-5d | Address: %d\n", level, address) +
                    "---".repeat(20);
        }
    }

    private final Map<String, Row> rows = new java.util.HashMap<>();
    public final List<Row> table = new java.util.ArrayList<>();
    private int level = 0;
    private int address = 0;

    public void addConstant(String name, int value) {
        putRow(new ConstantRow(name, value));
    }
    public void addVariable(String name) {
        putRow(new VariableRow(name, level, address++));
    }
    public void enterProcedure(String name, int startAddress) {
        putRow(new ProcedureRow(name, level, startAddress));
        level++;
        address = 3;
    }
    private void putRow(Row row) {
        table.add(row);
        rows.put(row.name, row);
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
