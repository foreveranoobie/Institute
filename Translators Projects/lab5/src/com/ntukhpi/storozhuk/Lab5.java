package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.command.Command;
import com.ntukhpi.storozhuk.parser.CommandParser;
import com.ntukhpi.storozhuk.parser.GrammarParser;
import com.ntukhpi.storozhuk.parser.GrammarTypeParser;
import com.ntukhpi.storozhuk.parser.SyntaxParser;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Lab5 {

    private GrammarParser grammarParser = new GrammarParser();
    private Set<String> terminals = new HashSet<>();
    private Set<String> nonTerminals = new HashSet<>();
    private List<GrammarRule> rules = new LinkedList<>();
    private Set<String> productives = new HashSet<>();
    private Set<String> unreachables = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path to read the grammars from");
        List<String> lines = Files.readAllLines(Paths.get(scanner.nextLine()));
        Lab5 lab5 = new Lab5();
        lab5.parseRules(lines);
        for (GrammarRule rule : lab5.getRules()) {
            System.out.println(rule);
        }
        lab5.parseNonProductive();
        System.out.println("\nNon terminals: " + lab5.getNonTerminals());
        System.out.println("\nTerminals: " + lab5.getTerminals());
        System.out.println(
                "\nNon Productives: " + lab5.getNonTerminals().stream()
                        .filter(nonTerminal -> !lab5.getProductives().contains(nonTerminal))
                        .collect(
                                Collectors.toList()));
        lab5.parseUnreachables();
        System.out.println("\nUnreachable symbols: " + lab5.getUnreachables());
        Map<GrammarRule, String> firstsMap = new HashMap<>();
        GrammarTypeParser grammarTypeParser = new GrammarTypeParser();
        for (GrammarRule rule : lab5.getRules()) {
            firstsMap.putAll(grammarTypeParser.findFirsts(lab5.getRules(), rule));
        }
        System.out.println("\nFirst function");
        firstsMap.forEach(
                (key, value) -> System.out.printf("FIRST(%s -> %s) = %s\n", key.getLeftSymbol(), key.getRightRules(),
                        value));
        Map<String, Set<String>> nextMap = new HashMap<>();
        for (String nonTerminal : lab5.getNonTerminals()) {
            nextMap.put(nonTerminal, grammarTypeParser.findNext(lab5.getRules(), nonTerminal));
        }
        System.out.println("\nNext function:");
        nextMap.forEach((key, value) -> System.out.printf("NEXT(%s) = %s\n", key, value));
        Map<GrammarRule, Set<String>> chooseMap = new HashMap<>();
        for (GrammarRule rule : lab5.getRules()) {
            chooseMap.put(rule, grammarTypeParser.parseChoose(lab5.getRules(), rule));
        }
        System.out.println("\nChoice function:");
        chooseMap.forEach(
                (key, value) -> System.out.printf("CHOICE(%s -> %s) = %s\n", key.getLeftSymbol(), key.getRightRules(),
                        value));
        List<Command> commands = new LinkedList<>();
        CommandParser commandParser = new CommandParser();
        for (GrammarRule rule : lab5.getRules()) {
            commands.addAll(commandParser.parseCommand(rule, chooseMap));
            commandParser.parseTerminalCommands(rule, lab5.terminals, commands);
        }
        commands.add(new Command("$", "#", Arrays.asList("$"), true));
        System.out.println("\nCommands");
        commands.forEach(System.out::println);
        SyntaxParser syntaxParser = new SyntaxParser();
        while (true) {
            System.out.println("Enter the expression: ");
            String input = scanner.nextLine();
            if (syntaxParser.parseInputString(input, commands)) {
                System.out.println("Expression is correct!");
            } else {
                System.out.println("Expression is incorrect!");
            }
            System.out.println("Continue? (y/n)");
            if (!scanner.nextLine().equals("y")) {
                break;
            }
        }
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
