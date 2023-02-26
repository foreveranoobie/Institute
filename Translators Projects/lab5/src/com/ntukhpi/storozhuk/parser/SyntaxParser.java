package com.ntukhpi.storozhuk.parser;

import static com.ntukhpi.storozhuk.util.ParserUtil.isEmptyChain;

import com.ntukhpi.storozhuk.command.Command;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class SyntaxParser {

    public boolean parseInputString(String input, List<Command> commands) {
        LinkedList<String> inputSymbols = new LinkedList<>();
        Arrays.stream(input.split("")).forEach(inputSymbols::add);
        inputSymbols.add("$");
        LinkedList<String> shopSymbols = new LinkedList<>();
        shopSymbols.add("#");
        shopSymbols.add("I");
        for (; ; ) {
            if (inputSymbols.size() == 1 && isEmptyChain(inputSymbols.get(0)) && shopSymbols.isEmpty()) {
                return true;
            }
            String inputSymbol = inputSymbols.getFirst();
            String shopSymbol = shopSymbols.getLast();
            Command fitCommand = null;
            try {
                fitCommand = commands.stream().filter(command -> command.getInputSymbol().equals(inputSymbol)
                        && command.getShopSymbol().equals(shopSymbol)).findFirst().get();
            } catch (NoSuchElementException ex) {
                return false;
            } finally {
                printParserStage(fitCommand, inputSymbols, shopSymbols);
            }
            pushCommand(fitCommand, inputSymbols, shopSymbols);
        }
    }

    private void printParserStage(Command command, List<String> inputSymbols, List<String> shopSymbols) {
        System.out.printf("%-85s  |  %-35s |  %s\n", inputSymbols, shopSymbols, command);
    }

    private void pushCommand(Command command, LinkedList<String> inputSymbols, LinkedList<String> shopSymbols) {
        shopSymbols.removeLast();
        if (!command.isNotShiftHead()) {
            inputSymbols.removeFirst();
        }
        if (!isEmptyChain(command.getShopInputChain().get(0))) {
            command.getShopInputChain().forEach(shopSymbols::add);
        }
    }
}
