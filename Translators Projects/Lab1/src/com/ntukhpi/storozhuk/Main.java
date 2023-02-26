package com.ntukhpi.storozhuk;

import com.ntukhpi.storozhuk.lexis.LexisParser;
import com.ntukhpi.storozhuk.lexis.impl.LexisParserImpl;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LexisParser lexisParser = new LexisParserImpl();
        String expr;
        String userAnswer;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter the expression to parse:");
            expr = scanner.nextLine();
            List<String> result = lexisParser.parse(expr);
            System.out.println(result);
            System.out.println("Continue? (y/n)");
            userAnswer = scanner.nextLine();
        } while (userAnswer.equals("y"));
    }
}
