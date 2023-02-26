package com.ntukhpi.storozhuk.manager;

import com.ntukhpi.storozhuk.rule.GrammarEntry;
import java.util.HashSet;
import java.util.Set;

public class ManagerTableElement {

    private GrammarEntry grammarEntry;

    Set<ManagerTableElementValue> elementValues;

    public ManagerTableElement() {
        elementValues = new HashSet<>();
    }

    public ManagerTableElement(GrammarEntry grammarEntry, Set<ManagerTableElementValue> elementValues) {
        this.grammarEntry = grammarEntry;
        elementValues = new HashSet<>(elementValues);
    }

    public GrammarEntry getGrammarEntry() {
        return grammarEntry;
    }

    public void setGrammarEntry(GrammarEntry grammarEntry) {
        this.grammarEntry = grammarEntry;
    }

    public Set<ManagerTableElementValue> getElementValues() {
        return elementValues;
    }

    public void setElementValues(Set<ManagerTableElementValue> elementValues) {
        this.elementValues = new HashSet<>(elementValues);
    }
}
