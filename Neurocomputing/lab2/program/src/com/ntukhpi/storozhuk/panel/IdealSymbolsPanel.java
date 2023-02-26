package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.StringUtil.ASSOCIATIVE_NEURON_PREFIX;
import static com.ntukhpi.storozhuk.util.StringUtil.SENSOR_NEURON_PREFIX;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class IdealSymbolsPanel extends JPanel {

    private GridBagConstraints gridBagConstraints = new GridBagConstraints();
    private JTable table;
    private String[] columnNames = {"", "-1", "+1"};
    private Object[][] rowData = new Object[1][3];
    private final NumbersInfo numbersInfo;

    public IdealSymbolsPanel(NumbersInfo numbersInfo) {
        super();
        this.numbersInfo = numbersInfo;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        setLayout(new GridBagLayout());
        initIdealSymbolsTable();
        initRowNames();
    }

    private void initIdealSymbolsTable() {
        table = new JTable(rowData, columnNames);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(200, 50));
        gridBagConstraints.gridy = 0;
        add(scrollPane, gridBagConstraints);

        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 0;
        JButton saveSymbolsButton = new JButton();
        saveSymbolsButton.setText("Save");
        saveSymbolsButton.setPreferredSize(new Dimension(100, 25));
        saveSymbolsButton.addActionListener(event -> {
            String negativeReactiveSymbol = of(table.getValueAt(0, 1))
                    .map(Objects::toString)
                    .orElse("");
            numbersInfo.setNegativeReactiveSymbol(negativeReactiveSymbol);

            String positiveReactiveSymbol = of(table.getValueAt(0, 2))
                    .map(Objects::toString)
                    .orElse("");
            numbersInfo.setPositiveReactiveSymbol(positiveReactiveSymbol);
        });
        add(saveSymbolsButton, gridBagConstraints);
    }

    private void initRowNames() {
        TableModel tableModel = table.getModel();
        tableModel.setValueAt("Symbol", 0, 0);
    }
}
