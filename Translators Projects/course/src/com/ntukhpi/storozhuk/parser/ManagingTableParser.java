package com.ntukhpi.storozhuk.parser;

import static com.ntukhpi.storozhuk.util.ParserUtil.isEmptyChain;
import static com.ntukhpi.storozhuk.util.ParserUtil.isNonTerminal;
import static com.ntukhpi.storozhuk.util.ParserUtil.isTerminal;

import com.ntukhpi.storozhuk.magazine.Allow;
import com.ntukhpi.storozhuk.magazine.Folding;
import com.ntukhpi.storozhuk.magazine.Reject;
import com.ntukhpi.storozhuk.magazine.Transfer;
import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.manager.ManagerTableElementValue;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ManagingTableParser {

    public List<ManagerTableElement> parseManagingTable(Map<String, Set<String>> nextFunctionsMap,
            Map<GrammarEntry, Set<GrammarEntry>> parsedVNextMap, List<TransitionElement> transitionTable,
            List<GrammarRuleWithEntry> rulesWithEntries) {
        List<ManagerTableElement> managerTable = new LinkedList<>();
        for (TransitionElement transitionTableElement : transitionTable) {
            ManagerTableElement managerTableElement = new ManagerTableElement();
            GrammarEntry transitionTableRow = transitionTableElement.getGrammarEntry();
            managerTableElement.setGrammarEntry(transitionTableRow);
            Set<ManagerTableElementValue> managerTableElementValues = new HashSet<>();

            //If row is initial Non-Terminal
            //Reject all rows. Magazine's end symbol # should have ALLOW rule
            if (transitionTableRow.getSymbol().equals("I")) {
                transitionTableElement.getTransitions().forEach((symbol, transition) -> {
                    if (isTerminal(symbol)) {
                        managerTableElementValues.add(new ManagerTableElementValue(symbol, new Reject()));
                    }
                });
                managerTableElementValues.add(new ManagerTableElementValue("#", new Allow()));
            }
            //If row is not initial Non-terminal rule
            else {
                //Case when current entry symbol has any non-terminals with empty chains
                parsedVNextMap.get(transitionTableRow).stream()
                        .filter(rowTransition -> entryHasEmptyChain(rowTransition,
                                rulesWithEntries))
                        .forEach(rowTransition -> nextFunctionsMap.get(rowTransition.getSymbol())
                                .forEach(nextFunctionSymbol -> {
                                    GrammarRuleWithEntry emptyChainRule = getEmptyChainRule(rowTransition,
                                            rulesWithEntries);
                                    managerTableElementValues.add(new ManagerTableElementValue(nextFunctionSymbol,
                                            new Folding(emptyChainRule)));
                                    managerTableElementValues.add(
                                            new ManagerTableElementValue("#", new Folding(emptyChainRule)));
                                }));

                //Case when the symbol is contained in some chains as last elements
                List<GrammarRuleWithEntry> chainsWithLastCurrentElement
                        = getRulesWithEntryAsLast(transitionTableRow, rulesWithEntries);
                if (!chainsWithLastCurrentElement.isEmpty()) {
                    for (GrammarRuleWithEntry entryWithLastCurrentSymbol : chainsWithLastCurrentElement) {
                        //Retrieving all symbols from NEXT functions of Left symbol from received chains
                        for (String nextChainSymbol : nextFunctionsMap.get(
                                entryWithLastCurrentSymbol.getLeftSymbol())) {
                            managerTableElementValues.add(
                                    new ManagerTableElementValue(nextChainSymbol,
                                            new Folding(entryWithLastCurrentSymbol)));
                        }
                    }
                    managerTableElementValues.add(
                            new ManagerTableElementValue("#", new Folding(chainsWithLastCurrentElement.get(0))));
                } else {
                    //Case when symbol is contained in transition table
                    transitionTableElement.getTransitions().forEach((symbol, transition) -> {
                        if (isTerminal(symbol) && !entryHasEmptyChain(transition, rulesWithEntries)) {
                            managerTableElementValues.add(new ManagerTableElementValue(symbol, new Transfer()));
                        }
                    });
                }
            }

            managerTableElement.setElementValues(managerTableElementValues);
            managerTable.add(managerTableElement);
        }
        return managerTable;
    }

    private boolean entryHasEmptyChain(GrammarEntry entry, List<GrammarRuleWithEntry> rulesWithEntries) {
        return isNonTerminal(entry.getSymbol()) && rulesWithEntries.stream()
                .anyMatch(grammarRuleWithEntry -> grammarRuleWithEntry.getLeftSymbol().equals(entry.getSymbol()) &&
                        isEmptyChain(grammarRuleWithEntry.getGrammarEntries().get(0).getSymbol()));
    }

    private GrammarRuleWithEntry getEmptyChainRule(GrammarEntry entry, List<GrammarRuleWithEntry> rulesWithEntries) {
        return rulesWithEntries.stream()
                .filter(grammarRuleWithEntry -> grammarRuleWithEntry.getLeftSymbol().equals(entry.getSymbol())
                        && isEmptyChain(grammarRuleWithEntry.getGrammarEntries().get(0).getSymbol()))
                .findFirst().orElse(null);
    }

    private List<GrammarRuleWithEntry> getRulesWithEntryAsLast(GrammarEntry entry,
            List<GrammarRuleWithEntry> rulesWithEntries) {
        return rulesWithEntries.stream().filter(grammarRuleWithEntry -> {
            List<GrammarEntry> grammarEntries = grammarRuleWithEntry.getGrammarEntries();
            return grammarEntries.indexOf(entry) == grammarEntries.size() - 1;
        }).collect(Collectors.toList());
    }
}
