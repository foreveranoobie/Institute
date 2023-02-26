package com.ntukhpi.storozhuk.parser;

import static com.ntukhpi.storozhuk.util.ParserUtil.isNonTerminal;
import static com.ntukhpi.storozhuk.util.ParserUtil.isTerminal;
import static java.util.Optional.ofNullable;

import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarTypeWithEntriesParser {

    public Map<GrammarEntry, Set<GrammarEntry>> parseVFirsts(List<GrammarRuleWithEntry> rules,
            GrammarEntry currentEntry) {
        Map<GrammarEntry, Set<GrammarEntry>> toAddVFirsts = new HashMap<>();

        if (isTerminal(currentEntry.getSymbol())) {
            Set<GrammarEntry> toAddGrammarEntries = new HashSet<>();
            toAddGrammarEntries.add(currentEntry);
            toAddVFirsts.put(currentEntry, toAddGrammarEntries);
        } else if (isNonTerminal(currentEntry.getSymbol())) {
            for (GrammarRuleWithEntry grammarRuleWithEntry : rules) {
                //If non-terminal has own rule
                if (grammarRuleWithEntry.getLeftSymbol().equals(currentEntry.getSymbol())) {
                    GrammarEntry firstEntryInChain = grammarRuleWithEntry.getGrammarEntries().get(0);

                    Map<GrammarEntry, Set<GrammarEntry>> parsedSubFirsts = parseVFirsts(rules, firstEntryInChain);
                    parsedSubFirsts.values().forEach(value -> addMissingEntriesToChain(currentEntry, toAddVFirsts,
                            value));
                }
            }
        }
        return toAddVFirsts;
    }

    public Map<GrammarEntry, Set<GrammarEntry>> parseVNext(List<GrammarEntry> entryChain,
            Map<GrammarEntry, Set<GrammarEntry>> vFirsts) {
        Map<GrammarEntry, Set<GrammarEntry>> parsedVNext = new HashMap<>();
        for (int index = 0; index < entryChain.size() - 1; index++) {
            parsedVNext.put(entryChain.get(index), vFirsts.get(entryChain.get(index + 1)));
        }
        Set<GrammarEntry> emptyChainSet = new HashSet<>();
        emptyChainSet.add(new GrammarEntry("$", 1));
        parsedVNext.put(entryChain.get(entryChain.size() - 1), emptyChainSet);
        return parsedVNext;
    }

    private Set<GrammarEntry> getAllEntriesForSymbol(List<GrammarRuleWithEntry> rules, String symbol) {
        Set<GrammarEntry> entries = new HashSet<>();
        rules.stream().filter(rule -> rule.getLeftSymbol().equals(symbol))
                .forEach(rule -> entries.addAll(rule.getGrammarEntries()));
        return entries;
    }

    private void addMissingEntriesToChain(GrammarEntry entry, Map<GrammarEntry, Set<GrammarEntry>> currentChains,
            Set<GrammarEntry> toAdd) {
        if (!toAdd.isEmpty()) {
            Set<GrammarEntry> chain = currentChains.computeIfAbsent(entry, k -> new HashSet<>());
            chain.addAll(toAdd);
            chain.add(entry);
        }
    }
}
