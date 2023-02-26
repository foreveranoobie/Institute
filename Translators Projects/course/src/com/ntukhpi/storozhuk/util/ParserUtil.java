package com.ntukhpi.storozhuk.util;

public class ParserUtil {

    public static boolean isTerminal(String symbol) {
        return !symbol.matches("[A-Z]") && !isEmptyChain(symbol);
    }

    public static boolean isNonTerminal(String symbol) {
        return symbol.matches("[A-Z]");
    }

    public static boolean isEmptyChain(String symbol) {
        return symbol.equals("$");
    }
}
