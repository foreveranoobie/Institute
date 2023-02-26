package com.ntukhpi.storozhuk.parser;

import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GrammarParser {

    public void parseRule(String line, Set<String> terminal, Set<String> nonTerminal, List<GrammarRule> rules) {
        String left = line.split("->")[0];
        String[] right = line.split("->")[1].split("\\|");
        if (right.length > 1) {
            for (String subRule : right) {
                parseRule(left.concat("->").concat(subRule), terminal, nonTerminal, rules);
            }
        } else {
            List<String> rightSymbols = new ArrayList<>();
            nonTerminal.add(left);
            for (String symbol : right[0].split("")) {
                if (symbol.matches("[A-Z]")) {
                    nonTerminal.add(symbol);
                    rightSymbols.add(symbol);
                } else if (!symbol.equals(" ")) {
                    if (!symbol.equals("$")) {
                        terminal.add(symbol);
                    }
                    rightSymbols.add(symbol);
                }
            }
            rules.add(new GrammarRule(left, rightSymbols));
        }
    }

    public Set<String> parseNonProductive(List<GrammarRule> rules, Set<String> terminals, Set<String> nonTerminals) {
        Set<String> productiveSymbols = new HashSet<>();
        for (GrammarRule rule : rules) {
            if (rule.getRightRules().stream().anyMatch(terminals::contains)) {
                productiveSymbols.add(rule.getLeftSymbol());
            }

        }
        boolean newProductiveFound = nonTerminals.containsAll(productiveSymbols);
        while (newProductiveFound) {
            newProductiveFound = false;
            for (GrammarRule rule : rules) {
                if (!productiveSymbols.contains(rule.getLeftSymbol()) &&
                        productiveSymbols.containsAll(rule.getRightRules().stream().filter(nonTerminals::contains)
                                .collect(Collectors.toSet()))) {
                    newProductiveFound = true;
                    productiveSymbols.add(rule.getLeftSymbol());
                }
            }
        }
        return productiveSymbols;
    }

    public Set<String> parseUnreachable(List<GrammarRule> rules, Set<String> terminals, Set<String> nonTerminals) {
        Set<String> reachableSymbols = new HashSet<>();
        reachableSymbols.add("I");
        boolean newReachableFound = true;
        while (newReachableFound) {
            newReachableFound = false;
            for (GrammarRule rule : rules) {
                if (reachableSymbols.contains(rule.getLeftSymbol())
                        && rule.getRightRules().stream().anyMatch(rightRule -> !reachableSymbols.contains(rightRule))) {
                    newReachableFound = true;
                    reachableSymbols.addAll(rule.getRightRules());
                }
            }
        }
        Set<String> unreachableSymbols = terminals.stream().filter(terminal -> !reachableSymbols.contains(terminal))
                .collect(Collectors.toSet());
        unreachableSymbols.addAll(nonTerminals.stream().filter(nonTerminal -> !reachableSymbols.contains(nonTerminal))
                .collect(Collectors.toSet()));
        return unreachableSymbols;
    }
}
