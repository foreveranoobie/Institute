package com.ntukhpi.storozhuk.parser;

import static java.util.Optional.ofNullable;

import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GrammarEntryParser {

    public GrammarRuleWithEntry getRuleWithGrammarEntries(GrammarRule currentRule,
            Map<String, Integer> symbolsCount) {
        List<GrammarEntry> newRightPart = new LinkedList<>();
        for (String symbol : currentRule.getRightRules()) {
            ofNullable(symbolsCount.get(symbol))
                    .ifPresentOrElse(symbolCount -> {
                                newRightPart.add(new GrammarEntry(symbol, ++symbolCount));
                                symbolsCount.put(symbol, symbolCount);
                            },
                            //if symbol is not present - add to map with number 1
                            () -> {
                                newRightPart.add(new GrammarEntry(symbol, 1));
                                symbolsCount.put(symbol, 1);
                            });
        }
        return new GrammarRuleWithEntry(currentRule.getLeftSymbol(), newRightPart);
    }
}
