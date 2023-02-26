package com.ntukhpi.storozhuk.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Command {

    private String inputSymbol;
    private String shopSymbol;
    private List<String> shopInputChain;
    private boolean shiftHead;

    public Command() {
        inputSymbol = "";
        shopSymbol = "";
        shopInputChain = new ArrayList<>();
        shiftHead = false;
    }

    public Command(String inputSymbol, String shopSymbol, List<String> shopInputChain, boolean shiftHead) {
        this.inputSymbol = inputSymbol;
        this.shopSymbol = shopSymbol;
        this.shopInputChain = shopInputChain;
        this.shiftHead = shiftHead;
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
                && Objects.equals(shiftHead, command.shiftHead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputSymbol, shopSymbol, shopInputChain, shiftHead);
    }

    public boolean isShiftHead() {
        return shiftHead;
    }

    public void setShiftHead(boolean shiftHead) {
        this.shiftHead = shiftHead;
    }

    public String toString() {
        String function = isShiftHead() ? "f*" : "f";
        String inputChain = shopInputChain.stream().reduce(String::concat).orElse("");
        return String.format("%s(s0, %s, %s) = (s1, %s)", function, inputSymbol, shopSymbol, inputChain);
    }
}
