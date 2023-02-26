package com.ntukhpi.storozhuk.rule;

import java.util.Objects;

public class GrammarEntry {

    String symbol;
    int number;

    public GrammarEntry() {
    }

    public GrammarEntry(String symbol, int number) {
        this.symbol = symbol;
        this.number = number;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrammarEntry that = (GrammarEntry) o;
        return number == that.number && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, number);
    }

    @Override
    public String toString() {
        String output;
        output = String.format("%s(%d)", symbol, number);
        return output;
    }
}
