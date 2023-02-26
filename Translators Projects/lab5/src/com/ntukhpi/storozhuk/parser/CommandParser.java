package com.ntukhpi.storozhuk.parser;

import static com.ntukhpi.storozhuk.util.ParserUtil.isEmptyChain;
import static com.ntukhpi.storozhuk.util.ParserUtil.isNonTerminal;
import static com.ntukhpi.storozhuk.util.ParserUtil.isTerminal;

import com.ntukhpi.storozhuk.command.Command;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandParser {

    public List<Command> parseCommand(GrammarRule rule, Map<GrammarRule, Set<String>> chooseMap) {
        String firstSymbol = rule.getRightRules().get(0);
        List<String> reverseRightPart = new ArrayList<>(rule.getRightRules());
        Collections.reverse(reverseRightPart);
        List<Command> commands = new ArrayList<>();
        if (isTerminal(firstSymbol)) {
            reverseRightPart.remove(reverseRightPart.size() - 1);
            if (reverseRightPart.size() == 0) {
                reverseRightPart.add("$");
            }
            commands.add(new Command(firstSymbol, rule.getLeftSymbol(), reverseRightPart, false));
        } else if (isNonTerminal(firstSymbol)) {
            chooseMap.get(rule).forEach(terminal ->
                    commands.add(new Command(terminal, rule.getLeftSymbol(), reverseRightPart, true)));
        } else if (isEmptyChain(firstSymbol)) {
            chooseMap.get(rule).forEach(terminal ->
                    commands.add(new Command(terminal, rule.getLeftSymbol(), Arrays.asList("$"), true)));
        }
        return commands;
    }

    public void parseTerminalCommands(GrammarRule rule, Set<String> terminals,
            List<Command> existingCommands) {
        List<String> rightPartReduced = new ArrayList<>(rule.getRightRules());
        rightPartReduced.remove(0);
        for (String symbol : rightPartReduced) {
            if (terminals.contains(symbol)) {
                Command command = new Command(symbol, symbol, Arrays.asList("$"), false);
                if (!existingCommands.contains(command)) {
                    existingCommands.add(command);
                }
            }
        }
    }
}
