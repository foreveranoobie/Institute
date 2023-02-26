package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.parser.GrammarParser;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("expr.txt"));
        Set<String> terminals = new HashSet<>();
        Set<String> nonTerminals = new HashSet<>();
        List<GrammarRule> rules = new LinkedList<>();
        GrammarParser grammarParser = new GrammarParser();
        for (String line : lines) {
            grammarParser.parseRule(line, terminals, nonTerminals, rules);
        }
        Set<String> productives = grammarParser.parseNonProductive(rules, terminals, nonTerminals);
        System.out.println("Non terminals: " + nonTerminals);
        System.out.println("Terminals: " + terminals);
        System.out.println(
                "Non Productives: " + nonTerminals.stream().filter(nonTerminal -> !productives.contains(nonTerminal))
                        .collect(
                                Collectors.toList()));
        for (GrammarRule rule : rules) {
            System.out.println(rule);
        }
        System.out.println("Unreachable: " + grammarParser.parseUnreachable(rules, terminals, nonTerminals));
    }
}
