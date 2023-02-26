package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.command.Command;
import com.ntukhpi.storozhuk.parser.CommandParser;
import com.ntukhpi.storozhuk.parser.GrammarParser;
import com.ntukhpi.storozhuk.parser.GrammarTypeParser;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Lab4 {

    private GrammarParser grammarParser = new GrammarParser();
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private List<GrammarRule> rules = new LinkedList<>();
    private Set<String> productives = new HashSet<>();
    private Set<String> unreachables = new HashSet<>();

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("test.txt"));
        //List<String> lines = Files.readAllLines(Paths.get("expr.txt"));
        Lab4 lab4 = new Lab4();
        lab4.parseRules(lines);
        lab4.parseNonProductive();
        System.out.println("Non terminals: " + lab4.getNonTerminals());
        System.out.println("Terminals: " + lab4.getTerminals());
        System.out.println(
                "Non Productives: " + lab4.getNonTerminals().stream()
                        .filter(nonTerminal -> !lab4.getProductives().contains(nonTerminal))
                        .collect(
                                Collectors.toList()));
        for (GrammarRule rule : lab4.getRules()) {
            System.out.println(rule);
        }
        System.out.println("Unreachable: " + lab4.getUnreachables());
        Map<GrammarRule, String> firstsMap = new HashMap<>();
        GrammarTypeParser grammarTypeParser = new GrammarTypeParser();
        for (GrammarRule rule : lab4.getRules()) {
            firstsMap.putAll(grammarTypeParser.findFirsts(lab4.getRules(), rule));
        }
        firstsMap.forEach((key, value) -> System.out.println(key + " | " + value));
        Map<String, Set<String>> nextMap = new HashMap<>();
        for (String nonTerminal : lab4.getNonTerminals()) {
            nextMap.put(nonTerminal, grammarTypeParser.findNext(lab4.getRules(), nonTerminal));
        }
        System.out.println("Next function:");
        nextMap.forEach((key, value) -> System.out.printf("%s: %s\n", key, value));
        Map<GrammarRule, Set<String>> chooseMap = new HashMap<>();
        for (GrammarRule rule : lab4.getRules()) {
            chooseMap.put(rule, grammarTypeParser.parseChoose(lab4.getRules(), rule));
        }
        System.out.println("Choose function:");
        chooseMap.forEach((key, value) -> System.out.printf("%s: %s\n", key, value));
        List<Command> commands = new LinkedList<>();
        CommandParser commandParser = new CommandParser();
        for (GrammarRule rule : lab4.getRules()) {
            commands.addAll(commandParser.parseCommand(rule, chooseMap));
            commandParser.parseTerminalCommands(rule, lab4.terminals, commands);
        }
        commands.add(new Command("", "#", Collections.emptyList(), true));
        System.out.println("Commands");
        commands.forEach(System.out::println);
    }

    public void parseRules(List<String> lines) {
        for (String line : lines) {
            grammarParser.parseRule(line, terminals, nonTerminals, rules);
        }
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
}
