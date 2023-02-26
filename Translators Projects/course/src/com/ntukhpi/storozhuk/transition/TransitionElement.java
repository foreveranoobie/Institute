package com.ntukhpi.storozhuk.transition;

import com.ntukhpi.storozhuk.rule.GrammarEntry;
import java.util.HashMap;
import java.util.Map;

public class TransitionElement {

    private GrammarEntry grammarEntry;
    private Map<String, GrammarEntry> transitions;

    public TransitionElement() {
        transitions = new HashMap<>();
    }

    public TransitionElement(GrammarEntry grammarEntry, Map<String, GrammarEntry> transitions) {
        this.grammarEntry = grammarEntry;
        this.transitions = new HashMap<>(transitions);
    }

    public GrammarEntry getGrammarEntry() {
        return grammarEntry;
    }

    public void setGrammarEntry(GrammarEntry grammarEntry) {
        this.grammarEntry = grammarEntry;
    }

    public Map<String, GrammarEntry> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, GrammarEntry> transitions) {
        this.transitions = transitions;
    }

    public void addTransition(String symbol, GrammarEntry entry) {
        transitions.put(symbol, entry);
    }

    @Override
    public String toString() {
        StringBuilder rows = new StringBuilder();
        transitions.forEach((key, value) -> rows.append(String.format("%6s | %s\n", key, value)));
        return String.format("Grammar entry: %s\nSymbol | Transition\n%s", grammarEntry, rows);
    }
}
