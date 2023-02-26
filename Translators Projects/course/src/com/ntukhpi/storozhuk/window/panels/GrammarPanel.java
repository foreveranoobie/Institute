package com.ntukhpi.storozhuk.window.panels;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.magazine.processor.MagazineData;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.rule.GrammarRule;
import com.ntukhpi.storozhuk.rule.GrammarRuleWithEntry;
import com.ntukhpi.storozhuk.window.CustomWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GrammarPanel extends JPanel {

    private Course course;
    private CustomWindow parentWindow;
    JScrollPane rulesPane = new JScrollPane();
    JScrollPane nextPane = new JScrollPane();
    JScrollPane vFirstPane = new JScrollPane();
    JScrollPane vNextPane = new JScrollPane();

    public GrammarPanel(Course course, CustomWindow parentWindow) {
        this.course = course;
        this.parentWindow = parentWindow;
        initFileChooser();
    }

    private void initFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addActionListener((e) -> {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(fileChooser.getSelectedFile().toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            course.parseRules(lines);
            for (GrammarRule rule : course.getRules()) {
                System.out.println(rule);
            }
            course.parseNonProductive();
            System.out.println("\nNon terminals: " + course.getNonTerminals());
            System.out.println("\nTerminals: " + course.getTerminals());
            System.out.println(
                    "\nNon Productives: " + course.getNonTerminals().stream()
                            .filter(nonTerminal -> !course.getProductives().contains(nonTerminal))
                            .collect(
                                    Collectors.toList()));
            course.parseUnreachables();
            System.out.println("\nUnreachable symbols: " + course.getUnreachables());

            course.parseFirstFunctions();
            System.out.println("\nFirst function");
            course.getFirsts().forEach(
                    (key, value) -> System.out.printf("FIRST(%s -> %s) = %s\n", key.getLeftSymbol(),
                            key.getRightRules(),
                            value));

            System.out.println("\nNext function:");
            course.parseNextFunctions();
            course.getNextMap().forEach((key, value) -> System.out.printf("NEXT(%s) = %s\n", key, value));

            course.parseRulesWithGrammarEntries();
            System.out.println("\nParsed grammar entries:");
            course.getRulesWithEntries().forEach(System.out::println);

            course.parseVFirstFunctions();
            System.out.println("Parsed VFirsts:");
            course.getVFirstFunctions().forEach((key, value) -> System.out.printf("VFIRST(%s) -> %s\n", key, value));

            course.parseVNextFunctions();
            System.out.println("\nParsed VNext:");
            course.getVNextFunctions().forEach((key, value) -> System.out.printf("VNEXT(%s) -> %s\n", key, value));

            System.out.println("\nParsed Transition table:");
            course.parseTransitionsTable();
            course.getTransitionTable().forEach(System.out::println);

            course.parseManagingTable();

            remove(rulesPane);
            remove(nextPane);
            remove(vFirstPane);
            remove(vNextPane);

            initView();

            parentWindow.updatePanels();
            this.updateUI();
        });
        add(fileChooser);
    }

    private void initView() {
        printTableOfRules(course.getRulesWithEntries());
        printTableOfNextFunctions(course.getNextMap());
        printTableOfVFirstFunctions(course.getVFirstFunctions());
        printTableOfVNextFunctions(course.getVNextFunctions());
    }

    private void printTableOfVNextFunctions(Map<GrammarEntry, Set<GrammarEntry>> vNextFunctions) {
        JTable table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        String[] header = new String[2];
        header[0] = "Symbol";
        header[1] = "VNext Function";

        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        vNextFunctions.forEach((symbol, values) -> {
            Object[] rowData = new Object[2];
            rowData[0] = symbol;
            rowData[1] = values.stream().map(GrammarEntry::toString).reduce(String::concat).orElse("");
            dtm.addRow(rowData);
        });
        vNextPane = new JScrollPane(table);
        add(vNextPane);
    }

    private void printTableOfVFirstFunctions(Map<GrammarEntry, Set<GrammarEntry>> vFirstFunctions) {
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        String[] header = new String[2];
        header[0] = "Symbol";
        header[1] = "VFirst Function";

        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        vFirstFunctions.forEach((symbol, values) -> {
            Object[] rowData = new Object[2];
            rowData[0] = symbol;
            rowData[1] = values.stream().map(GrammarEntry::toString).reduce(String::concat).orElse("");
            dtm.addRow(rowData);
        });
        vFirstPane = new JScrollPane(table);
        add(vFirstPane);
    }

    private void printTableOfNextFunctions(Map<String, Set<String>> nextMap) {
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        String[] header = new String[2];
        header[0] = "Symbol";
        header[1] = "NEXT Function";

        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        nextMap.forEach((symbol, values) -> {
            Object[] rowData = new Object[2];
            rowData[0] = symbol;
            rowData[1] = values.stream().reduce(String::concat)
                    .orElse("");
            dtm.addRow(rowData);
        });
        nextPane = new JScrollPane(table);
        add(nextPane);
    }

    private void printTableOfRules(List<GrammarRuleWithEntry> grammarRules) {
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        String[] header = new String[2];
        header[0] = "№";
        header[1] = "Rule";

        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        for (GrammarRuleWithEntry grammarRule : grammarRules) {
            Object[] rowData = new Object[2];
            rowData[0] = grammarRule.getRuleNumber();
            rowData[1] = grammarRule.getLeftSymbol() + "->" + grammarRule.getGrammarEntries()
                    .stream().map(GrammarEntry::toString).reduce(String::concat)
                    .orElse("");
            dtm.addRow(rowData);
        }
        rulesPane = new JScrollPane(table);
        add(rulesPane);
    }

    public Course getCourse() {
        return course;
    }
}
