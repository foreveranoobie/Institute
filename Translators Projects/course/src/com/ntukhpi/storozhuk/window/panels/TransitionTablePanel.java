package com.ntukhpi.storozhuk.window.panels;

import static com.ntukhpi.storozhuk.util.ParserUtil.isEmptyChain;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.manager.ManagerTableElementValue;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TransitionTablePanel extends JPanel {

    private Course course;

    public TransitionTablePanel(Course course) {
        this.course = course;
        showTransitionTable();
    }

    private void showTransitionTable() {
        Map<String, Integer> headerNumbers = new HashMap<>();
        JTable table = new JTable();
        table.addColumn(new TableColumn());
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        int headerIndex = 0;
        String[] header = new String[course.getTerminals().size() + course.getNonTerminals().size() + 1];
        header[headerIndex++] = "";
        for (String nonTerminal : course.getNonTerminals()) {
            headerNumbers.put(nonTerminal, headerIndex);
            header[headerIndex++] = nonTerminal;
        }
        for (String terminal : course.getTerminals()) {
            headerNumbers.put(terminal, headerIndex);
            header[headerIndex++] = terminal;
        }
        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        for (TransitionElement transitionElement : course.getTransitionTable()) {
            Object[] rowData = new Object[course.getTerminals().size() + course.getNonTerminals().size() + 1];
            rowData[0] = transitionElement.getGrammarEntry().toString();
            transitionElement.getTransitions().forEach((symbol, transitionSymbol) -> {
                if(!isEmptyChain(symbol)){
                    rowData[headerNumbers.get(symbol)] = transitionSymbol.toString();
                }
            });
            dtm.addRow(rowData);
        }

        JScrollPane sp = new JScrollPane(table);
        add(sp);
    }
}
