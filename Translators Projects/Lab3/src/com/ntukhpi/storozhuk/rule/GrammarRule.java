package com.ntukhpi.storozhuk.rule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
        this.rightRules = new ArrayList<>(rightRules);
    }

    @Override
    public String toString() {
        return "GrammarRule: [" +
                "leftSymbol='" + leftSymbol + '\'' +
                ", rightRules=" + rightRules +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrammarRule rule = (GrammarRule) o;
        return Objects.equals(leftSymbol, rule.leftSymbol) && Objects.equals(rightRules,
                rule.rightRules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftSymbol, rightRules);
    }
}
