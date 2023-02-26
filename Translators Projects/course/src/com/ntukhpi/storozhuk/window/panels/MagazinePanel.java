package com.ntukhpi.storozhuk.window.panels;

import static com.ntukhpi.storozhuk.util.ParserUtil.isEmptyChain;

import com.ntukhpi.storozhuk.Course;
import com.ntukhpi.storozhuk.magazine.processor.MagazineData;
import com.ntukhpi.storozhuk.magazine.processor.MagazineProcessor;
import com.ntukhpi.storozhuk.rule.GrammarEntry;
import com.ntukhpi.storozhuk.transition.TransitionElement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class MagazinePanel extends JPanel {

    private Course course;

    private MagazineProcessor magazineProcessor;

    public MagazinePanel(Course course, MagazineProcessor magazineProcessor) {
        this.course = course;
        this.magazineProcessor = magazineProcessor;
        initView();
        setBounds(0, 0, 500, 450);
    }

    private void initView() {
        setSize(500, 450);

        JTextArea operationInput = new JTextArea(1, 20);
        operationInput.setSize(200, 100);
        operationInput.setMinimumSize(new Dimension(100, 50));
        operationInput.setBounds(0, 0, 400, 250);

        JButton button = new JButton();
        button.setText("Enter");
        button.setBounds(0, 350, 100, 75);
        button.addActionListener(e -> {
            List<MagazineData> processedData = magazineProcessor.process(course, operationInput.getText());
            printProcessedTable(processedData);
            this.updateUI();
        });

        add(button);
        add(operationInput);
    }

    private void printProcessedTable(List<MagazineData> processedData) {
        JTable table = new JTable();
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);

        //Set-up headers
        String[] header = new String[3];
        header[0] = "Input";
        header[1] = "Magazine";
        header[2] = "Operation";

        dtm.setColumnIdentifiers(header);
        table.setModel(dtm);

        //Adding rows
        for (MagazineData magazineData : processedData) {
            Object[] rowData = new Object[3];
            rowData[0] = magazineData.getInput().stream().reduce(String::concat).orElse("");
            rowData[1] = magazineData.getMagazineState().stream().map(GrammarEntry::toString).reduce(String::concat)
                    .orElse("");
            rowData[2] = magazineData.getMagazineOperation().toString();
            dtm.addRow(rowData);
        }
        JScrollPane sp = new JScrollPane(table);
        add(sp);
    }
}
