package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.MathUtil.DEFAULT_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.StringUtil.ASSOCIATIVE_NEURON_PREFIX;
import static com.ntukhpi.storozhuk.util.StringUtil.ASSOCIATIVE_TO_REACTIVE_WEIGHTS_FILENAME;
import static com.ntukhpi.storozhuk.util.StringUtil.REACTIVE_NEURON_PREFIX;
import static com.ntukhpi.storozhuk.util.StringUtil.SENSORY_TO_ASSOCIATIVE_WEIGHTS_FILENAME;
import static com.ntukhpi.storozhuk.util.StringUtil.SENSOR_NEURON_PREFIX;

import com.ntukhpi.storozhuk.analyzer.LearnAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class AssociativeToReactivePanel extends JPanel {

    private JTable table;
    private String[] columnNames;
    private Object[][] rowData;
    private BigDecimal[][] weights;
    private JButton generateRandomWeightsButton = new JButton();
    private GridBagConstraints verticalConstraints = new GridBagConstraints();
    private final NumbersInfo numbersInfo;

    private final LearnAnalyzer learnAnalyzer;

    public AssociativeToReactivePanel(NumbersInfo numbersInfo, LearnAnalyzer learnAnalyzer) {
        super();
        this.numbersInfo = numbersInfo;
        this.learnAnalyzer = learnAnalyzer;
        columnNames = new String[numbersInfo.getAssociativeNeuronsNumber() + 1];
        rowData = new Object[numbersInfo.getReactiveNeuronsNumber()][numbersInfo.getAssociativeNeuronsNumber() + 1];
        weights = new BigDecimal[numbersInfo.getReactiveNeuronsNumber()][numbersInfo.getAssociativeNeuronsNumber()];
        verticalConstraints.fill = GridBagConstraints.VERTICAL;
        initColumnNames();
        initWeightsTable();
        initRowNames();
        generateWeights();
        initRandomWeightsButton();
        initSaveWeightsButton();
    }

    private void initWeightsTable() {
        table = new JTable(rowData, columnNames);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 50));
        verticalConstraints.gridy = 0;
        add(scrollPane, verticalConstraints);
    }

    private void initColumnNames() {
        columnNames[0] = "Wk";
        for (int i = 1; i < numbersInfo.getAssociativeNeuronsNumber() + 1; i++) {
            columnNames[i] = ASSOCIATIVE_NEURON_PREFIX + (i);
        }
    }

    private void initRowNames() {
        TableModel tableModel = table.getModel();
        for (int i = 0; i < 1; i++) {
            String currentRowName = REACTIVE_NEURON_PREFIX + (i + 1);
            tableModel.setValueAt(currentRowName, i, 0);
        }
    }

    private void generateWeights() {
        TableModel tableModel = table.getModel();
        for (int c = 0; c < numbersInfo.getAssociativeNeuronsNumber(); c++) {
            BigDecimal value = BigDecimal.valueOf(0.01 + Math.random() * (0.95 - 0.01));
            weights[0][c] = value;
            tableModel.setValueAt(value.round(DEFAULT_BIG_DECIMAL_ROUND), 0, c + 1);
        }
        numbersInfo.setReactiveWeights(Arrays.asList(weights[0]));
    }

    private void initRandomWeightsButton() {
        generateRandomWeightsButton.setText("Generate Weights");
        generateRandomWeightsButton.addActionListener(event -> generateWeights());
        verticalConstraints.gridy = 1;
        add(generateRandomWeightsButton, verticalConstraints);
    }

    private void initSaveWeightsButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(85, 25));
        button.setText("Save");
        button.addActionListener(event -> {
            StringBuilder weightsString = new StringBuilder();
            Arrays.stream(weights[0]).forEach(weight -> weightsString.append(String.format("%s\n", weight.toString())));
            weightsString.deleteCharAt(weightsString.length() - 1);
            Path filePath = Path.of(ASSOCIATIVE_TO_REACTIVE_WEIGHTS_FILENAME);
            try {
                Files.write(filePath, weightsString.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        verticalConstraints.gridy = 2;
        add(button, verticalConstraints);

        button = new JButton();
        button.setPreferredSize(new Dimension(65, 25));
        button.setText("Load");
        button.addActionListener(event -> {
            Path filePath = Path.of(ASSOCIATIVE_TO_REACTIVE_WEIGHTS_FILENAME);
            try {
                String[] weightsString = Files.readString(filePath).split("\n");
                weights[0] = new BigDecimal[numbersInfo.getAssociativeNeuronsNumber()];
                TableModel tableModel = table.getModel();
                for (int c = 0; c < numbersInfo.getAssociativeNeuronsNumber(); c++) {
                    BigDecimal value = BigDecimal.valueOf(Float
                            .parseFloat((weightsString[c])));
                    weights[0][c] = value;
                    tableModel.setValueAt(value.round(DEFAULT_BIG_DECIMAL_ROUND), 0, c + 1);
                }
                numbersInfo.setReactiveWeights(Arrays.asList(weights[0]));
                updateUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        verticalConstraints.gridy = 3;
        add(button, verticalConstraints);
    }

    public void updateWeightsTable(List<BigDecimal> updatedWeights) {
        TableModel tableModel = table.getModel();
        int c = 0;
        for (BigDecimal updatedWeight : updatedWeights) {
            weights[0][c] = updatedWeight;
            tableModel.setValueAt(updatedWeight.round(DEFAULT_BIG_DECIMAL_ROUND), 0, c + 1);
            c++;
        }
        table.setModel(tableModel);
        numbersInfo.setReactiveWeights(Arrays.asList(weights[0]));
        updateUI();
    }
}
