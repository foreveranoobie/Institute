package com.ntukhpi.storozhuk.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Command {

    private String inputSymbol;
    private String shopSymbol;
    private List<String> shopInputChain;
    private boolean notShiftHead;

    public Command() {
        inputSymbol = "";
        shopSymbol = "";
        shopInputChain = new ArrayList<>();
        notShiftHead = false;
    }

    public Command(String inputSymbol, String shopSymbol, List<String> shopInputChain, boolean notShiftHead) {
        this.inputSymbol = inputSymbol;
        this.shopSymbol = shopSymbol;
        this.shopInputChain = shopInputChain;
        this.notShiftHead = notShiftHead;
    }

    public String getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(String inputSymbol) {
        this.inputSymbol = inputSymbol;
    }

    public String getShopSymbol() {
        return shopSymbol;
    }

    public void setShopSymbol(String shopSymbol) {
        this.shopSymbol = shopSymbol;
    }

    public List<String> getShopInputChain() {
        return shopInputChain;
    }

    public void setShopInputChain(List<String> shopInputChain) {
        this.shopInputChain = shopInputChain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        return Objects.equals(inputSymbol, command.inputSymbol) && Objects.equals(shopSymbol,
                command.shopSymbol) && Objects.equals(shopInputChain, command.shopInputChain)
                && Objects.equals(notShiftHead, command.notShiftHead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputSymbol, shopSymbol, shopInputChain, notShiftHead);
    }

    public boolean isNotShiftHead() {
        return notShiftHead;
    }

    public void setNotShiftHead(boolean notShiftHead) {
        this.notShiftHead = notShiftHead;
    }

    public String toString() {
        String function = isNotShiftHead() ? "f*" : "f";
        String inputChain = shopInputChain.stream().reduce(String::concat).orElse("");
        return String.format("%-2s(s0, %s, %s) = (s1, %s)", function, inputSymbol, shopSymbol, inputChain);
    }
}
