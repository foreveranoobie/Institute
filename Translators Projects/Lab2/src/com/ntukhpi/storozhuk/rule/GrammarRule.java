package com.ntukhpi.storozhuk.rule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GrammarRule {

    private String leftSymbol;
    private List<String> rightRules;

    public GrammarRule() {
        leftSymbol = "";
        rightRules = new LinkedList<>();
    }

    public GrammarRule(String leftSymbol, List<String> rightRules) {
        this.leftSymbol = leftSymbol;
        this.rightRules = new ArrayList<>(rightRules);
    }

    public String getLeftSymbol() {
        return leftSymbol;
    }

    public void setLeftSymbol(String leftSymbol) {
        this.leftSymbol = leftSymbol;
    }

    public List<String> getRightRules() {
        return rightRules;
    }

    public void setRightRules(List<String> rightRules) {
        this.rightRules = rightRules;
    }

    @Override
    public String toString() {
        return "GrammarRule: [" +
                "leftSymbol='" + leftSymbol + '\'' +
                ", rightRules=" + rightRules +
                ']';
    }
}
