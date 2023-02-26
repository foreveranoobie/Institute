package com.ntukhpi.storozhuk.parser;

import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GrammarTypeParser {

    public Map<GrammarRule, String> findFirsts(List<GrammarRule> rules, GrammarRule toParseRule) {
        Map<GrammarRule, String> parsedFirsts = new LinkedHashMap<>();
        if (isEmptyChain(toParseRule.getRightRules().get(0))) {
            return parsedFirsts;
        } else if (isTerminal(toParseRule.getRightRules().get(0))) {
            parsedFirsts.put(toParseRule, toParseRule.getRightRules().get(0));
            return parsedFirsts;
        } else {
            String nonTerminal = toParseRule.getRightRules().get(0);
            StringBuilder parsedStringFirsts = new StringBuilder();
            for (GrammarRule grammarRule : rules) {
                if (grammarRule.getLeftSymbol().equals(nonTerminal) && !grammarRule.equals(toParseRule)) {
                    if (isEmptyChain(grammarRule.getRightRules().get(0))) {
                        if (toParseRule.getRightRules().size() > 1) {
                            if (isTerminal(toParseRule.getRightRules().get(1))) {
                                parsedStringFirsts.append(toParseRule.getRightRules().get(1));
                            } else {
                                GrammarRule recursionRule = new GrammarRule("",
                                        Arrays.asList(toParseRule.getRightRules().get(1)));
                                findFirsts(rules, recursionRule).values().stream().forEach(parsedStringFirsts::append);
                            }
                        }
                    }
                    findFirsts(rules, grammarRule).values().stream().forEach(parsedStringFirsts::append);
                }
            }
            parsedFirsts.put(toParseRule, parsedStringFirsts.toString());
        }
        return parsedFirsts;
    }

    public Set<String> findNext(List<GrammarRule> rules, String toParseNonTerminal) {
        Set<String> parsedNext = new LinkedHashSet<>();
        if (!isEmptyChain(toParseNonTerminal)) {
            List<GrammarRule> containingRules = getGrammarsRightContainingNonTerminal(rules, toParseNonTerminal);
            if (containingRules.isEmpty()) {
                return parsedNext;
            }
            StringBuilder parsedNextStrings = new StringBuilder();
            for (GrammarRule rule : containingRules) {
                if (!isLastElementInRightPart(rule, toParseNonTerminal)) {
                    int indexOfNonTerminal = rule.getRightRules().indexOf(toParseNonTerminal);
                    List<String> rightPart = rule.getRightRules();
                    if (nextSymbolHasTerminalChain(rules, rule, indexOfNonTerminal)) {
                        List<GrammarRule> reducedRules = new LinkedList<>(rules);
                        reducedRules.remove(rule);
                        String nextSymbol = rightPart.get(rightPart.indexOf(toParseNonTerminal) + 1);
                        parsedNext.addAll(findNext(reducedRules, nextSymbol));
                    } else {
                        String nextSymbol = rightPart.get(rightPart.indexOf(toParseNonTerminal) + 1);
                        findFirsts(rules, new GrammarRule("", Collections.singletonList(nextSymbol)))
                                .values().forEach(parsedNextStrings::append);
                    }
                }
            }
            Arrays.stream(parsedNextStrings.toString().split("")).forEach(parsedNext::add);
        }
        return parsedNext;
    }

    public Set<String> parseChoose(List<GrammarRule> rules, GrammarRule currentRule) {
        Set<String> parsedChooses = new LinkedHashSet<>();
        if (ruleContainsOnlyEmptySymbol(currentRule)) {
            List<GrammarRule> rulesDuplicate = new ArrayList<>(rules);
            rulesDuplicate.remove(currentRule);
            parsedChooses.addAll(findNext(rulesDuplicate, currentRule.getLeftSymbol()));
        } else if (currentRule.getRightRules().size() > 0) {
            List<String> rightPart = currentRule.getRightRules();
            String firstSymbol = rightPart.get(0);
            Set<String> currentChoose = new LinkedHashSet<>();
            if (nonTerminalLeadsToEmptyChain(rules, firstSymbol)) {
                currentChoose.addAll(findNext(rules, firstSymbol));
            }
            currentChoose.addAll(findFirsts(rules, currentRule).values());
            parsedChooses.addAll(currentChoose);
        }
        return parsedChooses;
    }

    private boolean nextSymbolHasTerminalChain(List<GrammarRule> rules, GrammarRule rule, Integer initialSymbolIndex) {
        boolean isInSize = rule.getRightRules().size() > initialSymbolIndex;
        if (isInSize) {
            String toAnalyzeSymbol = rule.getRightRules().get(initialSymbolIndex + 1);
            return isNonTerminal(toAnalyzeSymbol) &&
                    initialSymbolIndex + 2 == rule.getRightRules().size();
        } else {
            return false;
        }
    }

    private boolean nonTerminalLeadsToEmptyChain(List<GrammarRule> rules, String symbol) {
        return isNonTerminal(symbol) && rules.stream().anyMatch(rule -> rule.getLeftSymbol().equals(symbol) &&
                ruleContainsOnlyEmptySymbol(rule));
    }

    private boolean isLastElementInRightPart(GrammarRule rule, String symbol) {
        return (rule.getRightRules().indexOf(symbol)) == (rule.getRightRules().size() - 1);
    }

    private List<GrammarRule> getGrammarsRightContainingNonTerminal(List<GrammarRule> rules, String nonTerminal) {
        return rules.stream()
                .filter(rule -> rule.getRightRules().contains(nonTerminal))
                .collect(Collectors.toList());
    }

    private boolean isTerminal(String symbol) {
        return !symbol.matches("[A-Z]");
    }

    private boolean isNonTerminal(String symbol) {
        return symbol.matches("[A-Z]");
    }

    private boolean isEmptyChain(String symbol) {
        return symbol.equals("$");
    }

    private boolean ruleContainsOnlyEmptySymbol(GrammarRule rule) {
        return rule.getRightRules().size() == 1 && rule.getRightRules().get(0).equals("$");
    }
}
