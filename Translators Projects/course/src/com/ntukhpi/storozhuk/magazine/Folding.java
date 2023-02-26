package com.ntukhpi.storozhuk.magazine;

import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import java.util.List;

public class Folding implements MagazineOperation {
    private GrammarRuleWithEntry ruleWithEntries;

    public Folding(){}

    public Folding(GrammarRuleWithEntry ruleWithEntries){
        this.ruleWithEntries = ruleWithEntries;
    }

    public GrammarRuleWithEntry getRuleWithEntries() {
        return ruleWithEntries;
    }

    public void setRuleWithEntries(GrammarRuleWithEntry ruleWithEntries) {
        this.ruleWithEntries = ruleWithEntries;
    }

    @Override
    public String toString() {
        return "F(" + ruleWithEntries.getRuleNumber() + ")";
    }

    @Override
    public String getOperationSymbol() {
        return "F";
    }
}
