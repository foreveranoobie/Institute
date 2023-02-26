package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.MathUtil.ONE_SYMBOL_BIG_DECIMAL_ROUND;
import static com.ntukhpi.storozhuk.util.StringUtil.LOAD_STRING;
import static com.ntukhpi.storozhuk.util.StringUtil.SAVE_STRING;
import static com.ntukhpi.storozhuk.util.StringUtil.SYMBOLS_FILE;
import static com.ntukhpi.storozhuk.util.StringUtil.SYMBOL_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.S_NEURON_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.U_SYMBOL_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.Z_NEURON_SIGN;
import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyzer.HammingAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class IdealSymbolsPanel extends JPanel {

    private final NumbersInfo numbersInfo;
    private final HammingAnalyzer hammingAnalyzer;

    private JPanel sNeuronsWeightsPanel;

    private Map<Integer, List<JLabel>> idealSymbolLabelsMap = new HashMap<>();
    private List<List<JLabel>> sNeuronsWeightsLabels = new ArrayList<>();
    private List<JLabel> zSymbolsInputLabels = new ArrayList<>();

    public IdealSymbolsPanel(NumbersInfo numbersInfo, HammingAnalyzer hammingAnalyzer) {
        super();
        this.numbersInfo = numbersInfo;
        this.hammingAnalyzer = hammingAnalyzer;

        JPanel submainPanel = new JPanel();

        BoxLayout panelLayout = new BoxLayout(submainPanel, BoxLayout.Y_AXIS);
        submainPanel.setLayout(panelLayout);

        submainPanel.add(initSymbolLabels());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initSaveSymbolsButton());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initLoadSymbolsButton());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initAnalyzeSymbolsButton());
        initTableOfSNeuronsWeights();
        submainPanel.add(sNeuronsWeightsPanel);
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        add(submainPanel);
    }

    private JPanel initSymbolLabels() {
        final JPanel panel = new JPanel();

        List<JPanel> symbolPanels = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            JPanel singleSymbolPanel = initSingleSymbolLabel(i);
            symbolPanels.add(singleSymbolPanel);
            panel.add(singleSymbolPanel);
        }

        return panel;
    }

    private JPanel initSingleSymbolLabel(int panelNumber) {
        Dimension preferredLabelSize = new Dimension(25, 25);

        JPanel symbolPanel = new JPanel();
        symbolPanel.setLayout(new GridBagLayout());

        GridBagConstraints horizontalConstraints = new GridBagConstraints();
        horizontalConstraints.gridx = 2;

        JLabel symbolTitle = new JLabel();
        symbolTitle.setHorizontalAlignment(CENTER);
        symbolTitle.setText(String.format("V%d", panelNumber));
        symbolTitle.setPreferredSize(preferredLabelSize);
        symbolPanel.add(symbolTitle, horizontalConstraints);

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);

        List<JLabel> labels = new ArrayList<>(numbersInfo.getSymbolsColumnsCount() * numbersInfo.getSymbolsRowsCount());
        for (int r = 0; r < numbersInfo.getSymbolsRowsCount(); r++) {
            for (int c = 0; c < numbersInfo.getSymbolsColumnsCount(); c++) {
                horizontalConstraints.gridx = c;
                horizontalConstraints.gridy = r + 1;
                JLabel label = new JLabel();
                label.setOpaque(false);
                label.setBackground(Color.WHITE);
                label.setPreferredSize(preferredLabelSize);
                label.addMouseListener(getSymbolCellMouseListener(label));
                label.setVisible(true);
                label.setBorder(layoutBorder);
                label.setHorizontalAlignment(CENTER);
                symbolPanel.add(label, horizontalConstraints);
                labels.add(label);
            }
        }

        idealSymbolLabelsMap.put(panelNumber, labels);

        return symbolPanel;
    }

    private JButton initAnalyzeSymbolsButton() {
        JButton analyzeButton = new JButton();
        analyzeButton.setText("Analyze");
        analyzeButton.setPreferredSize(new Dimension(85, 25));
        analyzeButton.addActionListener(event -> {
            List<List<Integer>> sBipolarInputsList = new ArrayList<>(6);

            for (Integer inputNumber : idealSymbolLabelsMap.keySet()) {
                List<Integer> sBipolarInputs = new ArrayList<>(
                        numbersInfo.getSymbolsColumnsCount() * numbersInfo.getSymbolsRowsCount());

                for (JLabel label : idealSymbolLabelsMap.get(inputNumber)) {
                    sBipolarInputs.add("X".equals(label.getText()) ? 1 : -1);
                }

                sBipolarInputsList.add(sBipolarInputs);
            }

            numbersInfo.setsBipolarInputs(sBipolarInputsList);
            hammingAnalyzer.calculateWeightsForS();
            updateSNeuronsWeightsTable();
        });
        analyzeButton.setAlignmentX(0.48f);
        return analyzeButton;
    }

    private void initTableOfSNeuronsWeights() {
        sNeuronsWeightsPanel = new JPanel();
        sNeuronsWeightsPanel.setLayout(new GridBagLayout());

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(45, 25);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JLabel emptyLabel = new JLabel();
        emptyLabel.setPreferredSize(preferredLabelSize);
        emptyLabel.setBorder(layoutBorder);
        sNeuronsWeightsPanel.add(emptyLabel, gridBagConstraints);

        for (int i = 0; i < numbersInfo.getTotalSymbolsCount(); i++) {
            JLabel headerLabel = new JLabel();
            headerLabel.setText(String.format("%s%d(%s%d)", SYMBOL_SIGN, i + 1, Z_NEURON_SIGN, i + 1));
            headerLabel.setPreferredSize(preferredLabelSize);
            headerLabel.setBorder(layoutBorder);
            headerLabel.setHorizontalAlignment(CENTER);

            gridBagConstraints.gridx = i + 1;
            sNeuronsWeightsPanel.add(headerLabel, gridBagConstraints);
        }
        for (int r = 0; r < numbersInfo.getsNeuronsCount(); r++) {
            gridBagConstraints.gridx = 0;
            JLabel label = new JLabel();
            label.setText(String.format("%s%d", S_NEURON_SIGN, r + 1));
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            gridBagConstraints.gridy = r + 1;
            sNeuronsWeightsPanel.add(label, gridBagConstraints);

            List<JLabel> labels = new ArrayList<>(numbersInfo.getTotalSymbolsCount());
            for (int c = 1; c < numbersInfo.getTotalSymbolsCount() + 1; c++) {
                label = new JLabel();
                label.setBorder(layoutBorder);
                label.setPreferredSize(preferredLabelSize);
                gridBagConstraints.gridx = c;
                sNeuronsWeightsPanel.add(label, gridBagConstraints);
                labels.add(label);
            }

            sNeuronsWeightsLabels.add(labels);
        }
    }

    private void updateSNeuronsWeightsTable() {
        int columnIndex = 0;
        int rowIndex = 0;
        for (List<BigDecimal> weights : numbersInfo.getWeightsForSNeurons()) {
            for (BigDecimal weight : weights) {
                List<JLabel> rowLabels = sNeuronsWeightsLabels.get(rowIndex++);
                rowLabels.get(columnIndex).setText(weight.round(ONE_SYMBOL_BIG_DECIMAL_ROUND).toString());
            }
            columnIndex++;
            rowIndex = 0;
        }
    }

    private JButton initSaveSymbolsButton() {
        JButton button = new JButton();
        button.setText(SAVE_STRING);
        button.addActionListener(event -> {
            StringBuilder output = new StringBuilder();
            for (List<JLabel> labels : idealSymbolLabelsMap.values()) {
                labels.forEach(label -> {
                    if ("X".equals(label.getText())) {
                        output.append("1");
                    } else {
                        output.append("0");
                    }
                });
                output.append("\n");
            }
            output.deleteCharAt(output.length() - 1);
            try {
                Files.write(Path.of(SYMBOLS_FILE), output.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button.setAlignmentX(CENTER_ALIGNMENT);
        return button;
    }

    private JButton initLoadSymbolsButton() {
        JButton button = new JButton();
        button.setText(LOAD_STRING);
        button.addActionListener(event -> {
            String fileContent = "";
            try {
                fileContent = Files.readString(Path.of(SYMBOLS_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> rows = List.of(fileContent.split("\n"));
            int rowNum = 0;
            int colNum = 0;
            for (String row : rows) {
                List<JLabel> rowLabels = idealSymbolLabelsMap.get(rowNum);
                for (String rowElem : row.split("")) {
                    rowLabels.get(colNum++).setText("1".equals(rowElem) ? "X" : "");
                }
                colNum = 0;
                rowNum++;
            }
        });
        button.setAlignmentX(CENTER_ALIGNMENT);
        return button;
    }

    private MouseListener getSymbolCellMouseListener(JLabel label) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String labelText = label.getText();
                if (labelText == null || labelText.isEmpty()) {
                    label.setText("X");
                } else {
                    label.setText(null);
                }
            }
        };
    }
}
