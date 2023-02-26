package com.ntukhpi.storozhuk.lexis.impl;

import com.ntukhpi.storozhuk.command.StateCommand;
import com.ntukhpi.storozhuk.context.Context;
import com.ntukhpi.storozhuk.context.states.CurrentState;
import com.ntukhpi.storozhuk.context.states.InputState;
import com.ntukhpi.storozhuk.context.states.OutputState;
import com.ntukhpi.storozhuk.lexis.LexisParser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexisParserImpl implements LexisParser {

    private Map<InputState, StateCommand> inputsExecutions;

    public LexisParserImpl() {
        initInputsExecutions();
    }

    @Override
    public List<String> parse(String input) {
        Context context = new Context();
        String[] splatInput = input.split("");
        for (int symIndex = 0; symIndex < splatInput.length; symIndex++) {
            InputState inputState = getInputState(splatInput[symIndex]);
            StateCommand stateCommand = inputsExecutions.get(inputState);
            if (stateCommand != null) {
                stateCommand.execute(context, symIndex, splatInput);
            }
            if (context.getOutputState().equals(OutputState.PARSE_ERROR)) {
                break;
            }
        }
        if (context.getBracketsCount() != 0) {
            System.out.println("WRONG INPUT VALUE. OPEN AND CLOSE BRACKETS COUNT IS NOT EQUALS");
        }
        return context.getParsedLexemes();
    }

    private void initInputsExecutions() {
        inputsExecutions = new HashMap<>();
        inputsExecutions.put(InputState.SPACE_SYMBOL, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            if (currentState.equals(CurrentState.PARSING_IDENTIFIER)) {
                context.setCurrentState(CurrentState.WAITING_FOR_ASSIGNMENT);
                context.setOutputState(OutputState.IDENTIFIER_PARSED);
                context.pushParsedLexeme(" ");
                printState(context, InputState.SPACE_SYMBOL);
            } else {
                context.setOutputState(OutputState.NO_LEXEME_PARSED);
                printState(context, InputState.SPACE_SYMBOL);
            }
        });
        inputsExecutions.put(InputState.IDENTIFIER, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            if (currentStateIsEqualTo(currentState, CurrentState.WAITING_FOR_IDENTIFIER, CurrentState.PARSING_OPERATOR,
                    CurrentState.PARSING_OPEN_BRACKET, CurrentState.WAITING_FOR_LEXEME,
                    CurrentState.PARSING_ASSIGNMENT)) {
                if (!input[index].matches("[A-Za-z0-9]")) {
                    context.setOutputState(OutputState.PARSE_ERROR);
                    return;
                } else {
                    context.pushParsedLexeme(input[index]);
                    if (currentState.equals(CurrentState.WAITING_FOR_IDENTIFIER)) {
                        context.setCurrentState(CurrentState.PARSING_IDENTIFIER);
                    } else {
                        context.setCurrentState(CurrentState.PARSING_OPERAND);
                    }
                }
            } else if (currentStateIsEqualTo(currentState, CurrentState.PARSING_IDENTIFIER,
                    CurrentState.PARSING_OPERAND)) {
                context.updateLastParsedLexeme(input[index]);
            } else {
                context.setOutputState(OutputState.PARSE_ERROR);
                context.pushParsedLexeme(input[index]);
                printState(context, InputState.IDENTIFIER);
                return;
            }
            if (index == input.length - 1 || (index < input.length - 1 && !input[index + 1].matches("[A-Za-z0-9]"))) {
                InputState inputState = context.getCurrentState() == CurrentState.PARSING_OPERAND ? InputState.OPERAND :
                        InputState.IDENTIFIER;
                context.setOutputState(OutputState.LEXEME_PARSED);
                printState(context, inputState);
            } else {
                context.setOutputState(OutputState.NO_LEXEME_PARSED);
            }
        });
        inputsExecutions.put(InputState.ASSIGNMENT_SIGN, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            if (currentStateIsEqualTo(currentState, CurrentState.PARSING_IDENTIFIER,
                    CurrentState.WAITING_FOR_ASSIGNMENT)) {
                context.pushParsedLexeme(input[index]);
                context.setCurrentState(CurrentState.PARSING_ASSIGNMENT);
                context.setOutputState(OutputState.LEXEME_PARSED);
                printState(context, InputState.ASSIGNMENT_SIGN);
            } else {
                context.setOutputState(OutputState.PARSE_ERROR);
                context.pushParsedLexeme(input[index]);
                printState(context, InputState.ASSIGNMENT_SIGN);
            }
        });
        inputsExecutions.put(InputState.OPERATION, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            context.pushParsedLexeme(input[index]);
            if (currentStateIsEqualTo(currentState, CurrentState.PARSING_OPERAND,
                    CurrentState.PARSING_CLOSE_BRACKET) && index != input.length - 1) {
                context.setCurrentState(CurrentState.PARSING_OPERATOR);
                context.setOutputState(OutputState.LEXEME_PARSED);
                printState(context, InputState.OPERATION);
            } else {
                context.setOutputState(OutputState.PARSE_ERROR);
                context.pushParsedLexeme(input[index]);
                printState(context, InputState.OPERATION);
            }
        });
        inputsExecutions.put(InputState.LEFT_BRACKET, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            if (currentStateIsEqualTo(currentState, CurrentState.PARSING_ASSIGNMENT, CurrentState.PARSING_OPERATOR,
                    CurrentState.PARSING_OPEN_BRACKET)) {
                context.pushParsedLexeme(input[index]);
                context.setOutputState(OutputState.LEXEME_PARSED);
                context.setCurrentState(CurrentState.PARSING_OPEN_BRACKET);
                context.incrementBracketsCount();
                printState(context, InputState.LEFT_BRACKET);
            } else {
                context.setOutputState(OutputState.PARSE_ERROR);
                context.pushParsedLexeme(input[index]);
                printState(context, InputState.LEFT_BRACKET);
            }
        });
        inputsExecutions.put(InputState.RIGHT_BRACKET, (context, index, input) -> {
            CurrentState currentState = context.getCurrentState();
            if (currentStateIsEqualTo(currentState, CurrentState.PARSING_OPERAND, CurrentState.PARSING_OPEN_BRACKET)) {
                context.pushParsedLexeme(input[index]);
                context.setOutputState(OutputState.LEXEME_PARSED);
                context.setCurrentState(CurrentState.PARSING_CLOSE_BRACKET);
                context.decrementBracketsCount();
                printState(context, InputState.RIGHT_BRACKET);
            } else {
                context.setOutputState(OutputState.PARSE_ERROR);
                context.pushParsedLexeme(input[index]);
                printState(context, InputState.RIGHT_BRACKET);
            }
        });
    }

    private void printState(Context context, InputState inputState) {
        String lexeme = "";
        if (!context.getOutputState().equals(OutputState.NO_LEXEME_PARSED)) {
            lexeme = context.getLastLexeme();
        }
        System.out.printf("%-5s is %-15s with output state %-17s and current state %s\n",
                lexeme,
                inputState.toString(), context.getOutputState().toString(),
                context.getCurrentState().toString());
    }

    private InputState getInputState(String symbol) {
        if (symbol.matches("[A-Za-z0-9]")) {
            return InputState.IDENTIFIER;
        } else if (symbol.matches("[+\\-*/]")) {
            return InputState.OPERATION;
        } else if (symbol.equals("(")) {
            return InputState.LEFT_BRACKET;
        } else if (symbol.equals(")")) {
            return InputState.RIGHT_BRACKET;
        } else if (symbol.equals("=")) {
            return InputState.ASSIGNMENT_SIGN;
        } else if (symbol.equals(" ")) {
            return InputState.SPACE_SYMBOL;
        }
        return null;
    }

    private boolean currentStateIsEqualTo(CurrentState currentState, CurrentState... toCompareStates) {
        return Arrays.asList(toCompareStates).contains(currentState);
    }
}
