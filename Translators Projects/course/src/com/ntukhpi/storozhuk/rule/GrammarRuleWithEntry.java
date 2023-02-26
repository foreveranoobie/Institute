package com.ntukhpi.storozhuk.rule;

import java.util.List;

public class GrammarRuleWithEntry {

    private int ruleNumber;
    private String leftSymbol;
    private List<GrammarEntry> grammarEntries;

    public GrammarRuleWithEntry() {
    }

    public GrammarRuleWithEntry(String leftSymbol, List<GrammarEntry> grammarEntry) {
        this.leftSymbol = leftSymbol;
        this.grammarEntries = grammarEntry;
    }

    public String getLeftSymbol() {
        return leftSymbol;
    }

    public void setLeftSymbol(String leftSymbol) {
        this.leftSymbol = leftSymbol;
    }

    public List<GrammarEntry> getGrammarEntries() {
        return grammarEntries;
    }

    public void setGrammarEntries(List<GrammarEntry> grammarEntries) {
        this.grammarEntries = grammarEntries;
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    @Override
    public String toString() {
        return String.format("%d) %s -> %s", ruleNumber, leftSymbol, grammarEntries);
    }
}
