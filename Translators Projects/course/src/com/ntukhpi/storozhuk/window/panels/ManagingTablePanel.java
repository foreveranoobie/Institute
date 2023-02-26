package com.ntukhpi.storozhuk.window.panels;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.manager.ManagerTableElement;
import com.ntukhpi.storozhuk.manager.ManagerTableElementValue;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ManagingTablePanel extends JPanel {

    private Course course;

    public ManagingTablePanel(Course course) {
        this.course = course;
        showManagingTable();
        setSize(1650, 1050);
    }

    private void showManagingTable() {
        Map<String, Integer> headerTerminalsNumbers = new HashMap<>();
        JTable table = new JTable();
        table.addColumn(new TableColumn());
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        int headerIndex = 0;
        String[] header = new String[course.getTerminals().size() + 2];
        header[headerIndex++] = "";
        for (String terminal : course.getTerminals()) {
            headerTerminalsNumbers.put(terminal, headerIndex);
            header[headerIndex++] = terminal;
        }
        headerTerminalsNumbers.put("#", headerIndex);
        header[headerIndex] = "#";
        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        for (ManagerTableElement managerTableElement : course.getManagerTableElements()) {
            Object[] rowData = new Object[course.getTerminals().size() + 2];
            rowData[0] = managerTableElement.getGrammarEntry().toString();
            for (ManagerTableElementValue managerTableElementValue : managerTableElement.getElementValues()) {
                String columnSymbol = managerTableElementValue.getSymbol();
                if (!"$".equals(columnSymbol)) {
                    rowData[headerTerminalsNumbers.get(columnSymbol)] =
                            managerTableElementValue.getMagazineOperation().toString();
                }
            }
            dtm.addRow(rowData);
        }

        JScrollPane sp = new JScrollPane(table);
        add(sp);
    }
}
