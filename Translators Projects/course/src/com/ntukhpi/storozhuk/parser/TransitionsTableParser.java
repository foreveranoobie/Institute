package com.ntukhpi.storozhuk.parser;

import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransitionsTableParser {

    public List<TransitionElement> parseTransitionsTable(Map<GrammarEntry, Set<GrammarEntry>> vNextMap) {
        List<TransitionElement> transitionElements = new LinkedList<>();
        vNextMap.forEach(((grammarEntry, chainEntries) -> {
            TransitionElement transitionElement = new TransitionElement();
            transitionElement.setGrammarEntry(grammarEntry);
            chainEntries.forEach(chainEntry -> transitionElement.addTransition(chainEntry.getSymbol(), chainEntry));
            transitionElements.add(transitionElement);
        }));
        return transitionElements;
    }
}
