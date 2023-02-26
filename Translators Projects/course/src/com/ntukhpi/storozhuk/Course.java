package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.parser.GrammarEntryParser;
import com.ntukhpi.storozhuk.parser.GrammarParser;
import com.ntukhpi.storozhuk.parser.GrammarTypeParser;
import com.ntukhpi.storozhuk.parser.GrammarTypeWithEntriesParser;
import com.ntukhpi.storozhuk.parser.ManagingTableParser;
import com.ntukhpi.storozhuk.parser.TransitionsTableParser;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import com.ntukhpi.storozhuk.window.CustomWindow;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Course {

    //parsers
    private GrammarParser grammarParser = new GrammarParser();
    private GrammarTypeParser grammarTypeParser = new GrammarTypeParser();
    private GrammarEntryParser grammarEntryParser = new GrammarEntryParser();
    private GrammarTypeWithEntriesParser grammarTypeWithEntriesParser = new GrammarTypeWithEntriesParser();
    private TransitionsTableParser transitionsTableParser = new TransitionsTableParser();
    private ManagingTableParser managingTableParser = new ManagingTableParser();

    //data
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private List<GrammarRule> rules = new LinkedList<>();
    private Set<String> productives = new HashSet<>();
    private Set<String> unreachables = new HashSet<>();
    private Map<GrammarRule, String> firstsMap = new HashMap<>();
    private Map<String, Set<String>> nextMap = new HashMap<>();
    private List<GrammarRuleWithEntry> rulesWithEntries = new LinkedList<>();
    private Map<GrammarEntry, Set<GrammarEntry>> parsedVFirsts = new HashMap<>();
    private Map<GrammarEntry, Set<GrammarEntry>> parsedVNextMap = new HashMap<>();
    private List<TransitionElement> transitionTable = new LinkedList<>();
    private List<ManagerTableElement> managerTableElements = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path to read the grammars from");
        List<String> lines = Files.readAllLines(Paths.get(scanner.nextLine()));
        Course course = new Course();
        course.parseRules(lines);
        for (GrammarRule rule : course.getRules()) {
            System.out.println(rule);
        }
        course.parseNonProductive();
        System.out.println("\nNon terminals: " + course.getNonTerminals());
        System.out.println("\nTerminals: " + course.getTerminals());
        System.out.println(
                "\nNon Productives: " + course.getNonTerminals().stream()
                        .filter(nonTerminal -> !course.getProductives().contains(nonTerminal))
                        .collect(
                                Collectors.toList()));
        course.parseUnreachables();
        System.out.println("\nUnreachable symbols: " + course.getUnreachables());

        course.parseFirstFunctions();
        System.out.println("\nFirst function");
        course.getFirsts().forEach(
                (key, value) -> System.out.printf("FIRST(%s -> %s) = %s\n", key.getLeftSymbol(), key.getRightRules(),
                        value));

        System.out.println("\nNext function:");
        course.parseNextFunctions();
        course.getNextMap().forEach((key, value) -> System.out.printf("NEXT(%s) = %s\n", key, value));

        course.parseRulesWithGrammarEntries();
        System.out.println("\nParsed grammar entries:");
        course.getRulesWithEntries().forEach(System.out::println);

        course.parseVFirstFunctions();
        System.out.println("Parsed VFirsts:");
        course.getVFirstFunctions().forEach((key, value) -> System.out.printf("VFIRST(%s) -> %s\n", key, value));

        course.parseVNextFunctions();
        System.out.println("\nParsed VNext:");
        course.getVNextFunctions().forEach((key, value) -> System.out.printf("VNEXT(%s) -> %s\n", key, value));

        System.out.println("\nParsed Transition table:");
        course.parseTransitionsTable();
        course.getTransitionTable().forEach(System.out::println);

        course.parseManagingTable();*/

        CustomWindow customWindow = new CustomWindow(new Course());
    }

    public void parseRules(List<String> lines) {
        for (String line : lines) {
            grammarParser.parseRule(line, terminals, nonTerminals, rules);
        }
    }

    public void parseFirstFunctions() {
        for (GrammarRule rule : getRules()) {
            firstsMap.putAll(grammarTypeParser.findFirsts(getRules(), rule));
        }
    }

    public void parseNextFunctions() {
        for (String nonTerminal : getNonTerminals()) {
            nextMap.put(nonTerminal, grammarTypeParser.findNext(getRules(), nonTerminal));
        }
    }

    public void parseRulesWithGrammarEntries() {
        Map<String, Integer> numberOfEntries = new HashMap<>();
        int ruleNumber = 0;
        for (GrammarRule rule : getRules()) {
            GrammarRuleWithEntry parsedRule = grammarEntryParser.getRuleWithGrammarEntries(rule,
                    numberOfEntries);
            parsedRule.setRuleNumber(++ruleNumber);
            rulesWithEntries.add(parsedRule);
        }
    }

    public void parseVFirstFunctions() {
        for (GrammarRuleWithEntry ruleWithEntry : getRulesWithEntries()) {
            if (ruleWithEntry.getLeftSymbol().equals("I")) {
                parsedVFirsts.putAll(
                        grammarTypeWithEntriesParser.parseVFirsts(getRulesWithEntries(),
                                new GrammarEntry("I", 1)));
            }
            for (GrammarEntry grammarEntry : ruleWithEntry.getGrammarEntries()) {
                parsedVFirsts.putAll(grammarTypeWithEntriesParser
                        .parseVFirsts(getRulesWithEntries(), grammarEntry));
            }
        }
    }

    public void parseVNextFunctions() {
        for (GrammarRuleWithEntry ruleWithEntry : getRulesWithEntries()) {
            parsedVNextMap.putAll(
                    grammarTypeWithEntriesParser.parseVNext(ruleWithEntry.getGrammarEntries(), parsedVFirsts));
        }
        parsedVNextMap.putAll(
                grammarTypeWithEntriesParser.parseVNext(Arrays.asList(new GrammarEntry("I", 1)), parsedVFirsts));
        parsedVNextMap.put(new GrammarEntry("h", 1), parsedVFirsts.get(new GrammarEntry("I", 1)));
    }

    public void parseTransitionsTable() {
        transitionTable =
                new ArrayList<>(transitionsTableParser.parseTransitionsTable(getVNextFunctions()));
    }

    public void parseManagingTable() {
        managerTableElements = new ManagingTableParser().parseManagingTable(getNextMap(), getVNextFunctions(),
                getTransitionTable(), getRulesWithEntries());

    }

    public void parseNonProductive() {
        productives = grammarParser.parseNonProductive(rules, terminals, nonTerminals);
    }

    public void parseUnreachables() {
        unreachables = grammarParser.parseUnreachable(rules, terminals, nonTerminals);
    }

    public GrammarParser getGrammarParser() {
        return grammarParser;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<GrammarRule> getRules() {
        return rules;
    }

    public Set<String> getProductives() {
        return productives;
    }

    public Set<String> getUnreachables() {
        return unreachables;
    }

    public Map<GrammarRule, String> getFirsts() {
        return firstsMap;
    }

    public Map<String, Set<String>> getNextMap() {
        return nextMap;
    }

    public List<GrammarRuleWithEntry> getRulesWithEntries() {
        return rulesWithEntries;
    }

    public Map<GrammarEntry, Set<GrammarEntry>> getVFirstFunctions() {
        return parsedVFirsts;
    }

    public Map<GrammarEntry, Set<GrammarEntry>> getVNextFunctions() {
        return parsedVNextMap;
    }

    public List<TransitionElement> getTransitionTable() {
        return transitionTable;
    }

    public List<ManagerTableElement> getManagerTableElements() {
        return managerTableElements;
    }
}
