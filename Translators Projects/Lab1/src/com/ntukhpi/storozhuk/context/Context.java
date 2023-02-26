package com.ntukhpi.storozhuk.context;

import com.ntukhpi.storozhuk.context.states.CurrentState;
import com.ntukhpi.storozhuk.context.states.InputState;
import com.ntukhpi.storozhuk.context.states.OutputState;
import java.util.ArrayList;
import java.util.List;

public class Context {

    private int bracketsCount = 0;
    private OutputState outputState = OutputState.NO_LEXEME_PARSED;
    private CurrentState currentState = CurrentState.WAITING_FOR_IDENTIFIER;
    private List<String> parsed = new ArrayList<>();

    public void updateLastParsedLexeme(String toUpdate) {
        String getCurrent = parsed.get(parsed.size() - 1);
        parsed.set(parsed.size() - 1, getCurrent.concat(toUpdate));
    }

    public String getLastLexeme() {
        return parsed.get(parsed.size() - 1);
    }

    public void pushParsedLexeme(String toPush) {
        parsed.add(toPush);
    }

    public List<String> getParsedLexemes() {
        return parsed;
    }

    public void incrementBracketsCount() {
        bracketsCount++;
    }

    public void decrementBracketsCount() {
        bracketsCount--;
    }

    public int getBracketsCount() {
        return bracketsCount;
    }

    public OutputState getOutputState() {
        return outputState;
    }

    public void setOutputState(OutputState outputState) {
        this.outputState = outputState;
    }

    public CurrentState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CurrentState currentState) {
        this.currentState = currentState;
    }
}
