package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.StringUtil.ASSOCIATIVE_NEURON_PREFIX;
import static com.ntukhpi.storozhuk.util.StringUtil.SENSORY_TO_ASSOCIATIVE_WEIGHTS_FILENAME;
import static com.ntukhpi.storozhuk.util.StringUtil.SENSOR_NEURON_PREFIX;

import com.ntukhpi.storozhuk.analyzer.LearnAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class SensoryToAssociativePanel extends JPanel {

    private JTable table;
    private String[] columnNames;
    private Object[][] rowData;
    private List<BigDecimal> weightsList;
    private JButton generateRandomWeightsButton = new JButton();
    GridBagConstraints verticalConstraints = new GridBagConstraints();
    private NumbersInfo numbersInfo;

    private final LearnAnalyzer learnAnalyzer;

    public SensoryToAssociativePanel(LearnAnalyzer learnAnalyzer, NumbersInfo numbersInfo) {
        super();
        this.learnAnalyzer = learnAnalyzer;
        this.numbersInfo = numbersInfo;
        columnNames = new String[numbersInfo.getSensoryNeuronsNumber() + 1];
        rowData = new Object[numbersInfo.getAssociativeNeuronsNumber()][numbersInfo.getSensoryNeuronsNumber() + 1];
        verticalConstraints.fill = GridBagConstraints.VERTICAL;
        initColumnNames();
        initWeightsTable();
        initRowNames();
        generateWeights();
        initRandomWeightsButton();
        initSaveWeightsButton();
        initLoadSavedWeightsButton();
    }

    private void initWeightsTable() {
        setLayout(new GridBagLayout());
        table = new JTable(rowData, columnNames);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.addPropertyChangeListener("tableCellEditor", listener -> {
            if (!table.isEditing()) {
                weightsList = new ArrayList<>(
                        numbersInfo.getAssociativeNeuronsNumber() * numbersInfo.getSensoryNeuronsNumber());
                for (int r = 0; r < numbersInfo.getAssociativeNeuronsNumber(); r++) {
                    for (int c = 1; c < numbersInfo.getSensoryNeuronsNumber() + 1; c++) {
                        weightsList.add(BigDecimal.valueOf(Float.parseFloat(table.getValueAt(r, c).toString())));
                    }
                }
                learnAnalyzer.setSensoryToAssociativeWeights(weightsList);
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1150, 200));
        verticalConstraints.gridy = 0;
        add(scrollPane, verticalConstraints);
    }

    private void initColumnNames() {
        columnNames[0] = "Wij";
        for (int i = 2; i < 27; i++) {
            columnNames[i - 1] = SENSOR_NEURON_PREFIX + (i - 1);
        }
    }

    private void initRowNames() {
        TableModel tableModel = table.getModel();
        for (int i = 0; i < numbersInfo.getAssociativeNeuronsNumber(); i++) {
            String currentRowName = ASSOCIATIVE_NEURON_PREFIX + (i + 1);
            tableModel.setValueAt(currentRowName, i, 0);
        }
    }

    private void generateWeights() {
        weightsList = new ArrayList<>(
                numbersInfo.getAssociativeNeuronsNumber() * numbersInfo.getSensoryNeuronsNumber());
        TableModel tableModel = table.getModel();
        for (int r = 0; r < numbersInfo.getAssociativeNeuronsNumber(); r++) {
            for (int c = 0; c < numbersInfo.getSensoryNeuronsNumber(); c++) {
                BigDecimal value = BigDecimal.valueOf(0.25 + Math.random() * (0.75 - 0.25));
                weightsList.add(value);
                tableModel.setValueAt(value.round(DEFAULT_BIG_DECIMAL_ROUND), r, c + 1);
            }
        }
        learnAnalyzer.setSensoryToAssociativeWeights(weightsList);
    }

    private void initRandomWeightsButton() {
        generateRandomWeightsButton.setText("Generate Weights");
        generateRandomWeightsButton.addActionListener(event -> generateWeights());
        verticalConstraints.gridy = 1;
        add(generateRandomWeightsButton, verticalConstraints);
    }

    private void initSaveWeightsButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(65, 25));
        button.setText("Save");
        button.addActionListener(event -> {
            StringBuilder weights = new StringBuilder();
            weightsList.forEach(weight -> weights.append(String.format("%s\n", weight.toString())));
            weights.deleteCharAt(weights.length() - 1);
            Path filePath = Path.of(SENSORY_TO_ASSOCIATIVE_WEIGHTS_FILENAME);
            try {
                Files.write(filePath, weights.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        verticalConstraints.gridy = 2;
        add(button, verticalConstraints);
    }

    private void initLoadSavedWeightsButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(65, 25));
        button.setText("Load");
        button.addActionListener(event -> {
            Path filePath = Path.of(SENSORY_TO_ASSOCIATIVE_WEIGHTS_FILENAME);
            try {
                String[] weights = Files.readString(filePath).split("\n");
                weightsList = new ArrayList<>(
                        numbersInfo.getAssociativeNeuronsNumber() * numbersInfo.getSensoryNeuronsNumber());
                TableModel tableModel = table.getModel();
                for (int r = 0; r < numbersInfo.getAssociativeNeuronsNumber(); r++) {
                    for (int c = 0; c < numbersInfo.getSensoryNeuronsNumber(); c++) {
                        BigDecimal value = BigDecimal.valueOf(Float
                                .parseFloat((weights[r * numbersInfo.getSensoryNeuronsNumber() + c])));
                        weightsList.add(value);
                        tableModel.setValueAt(value.round(DEFAULT_BIG_DECIMAL_ROUND), r, c + 1);
                    }
                }
                learnAnalyzer.setSensoryToAssociativeWeights(weightsList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        verticalConstraints.gridy = 3;
        add(button, verticalConstraints);
    }

    public List<BigDecimal> getWeightsList() {
        return weightsList;
    }
}
