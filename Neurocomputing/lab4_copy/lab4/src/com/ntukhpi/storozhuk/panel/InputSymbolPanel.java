package com.ntukhpi.storozhuk.panel;

import static java.util.Optional.ofNullable;
import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyze.HopfieldAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class InputSymbolPanel extends JPanel {

    private final NumbersInfo numbersInfo;
    private final HopfieldAnalyzer hopfieldAnalyzer;
    private List<JLabel> inputSymbolLabels;

    private JPanel submainPanel;
    private JPanel thetaCheckBoxPanel;
    private JRadioButton firstThetaRadioButton;
    private JRadioButton secondThetaRadioButton;

    private JLabel resultLabel;

    private JPanel resultPanel;

    public InputSymbolPanel(NumbersInfo numbersInfo, HopfieldAnalyzer hopfieldAnalyzer) {
        this.numbersInfo = numbersInfo;
        this.hopfieldAnalyzer = hopfieldAnalyzer;
        inputSymbolLabels = new ArrayList<>(numbersInfo.getColumnsCount() * numbersInfo.getRowsCount());

        submainPanel = new JPanel();
        BoxLayout panelLayout = new BoxLayout(submainPanel, BoxLayout.Y_AXIS);
        submainPanel.setLayout(panelLayout);

        submainPanel.add(initSymbolInputPanel());
        initResultSymbolLabel();
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        initThetaRadioButtonsPanel();
        submainPanel.add(thetaCheckBoxPanel);
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initAnalyzeInputButton());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        add(submainPanel);
    }

    private void initResultSymbolLabel() {
        resultLabel = new JLabel();
        resultLabel.setPreferredSize(new Dimension(80, 20));
        resultLabel.setVisible(false);
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(resultLabel);
    }

    private JPanel initSymbolInputPanel() {
        JPanel symbolPanel = new JPanel();
        symbolPanel.setLayout(new GridBagLayout());

        GridBagConstraints horizontalConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(25, 25);

        for (int r = 0; r < numbersInfo.getRowsCount(); r++) {
            for (int c = 0; c < numbersInfo.getColumnsCount(); c++) {
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
                inputSymbolLabels.add(label);
            }
        }
        return symbolPanel;
    }

    private void initThetaRadioButtonsPanel() {
        thetaCheckBoxPanel = new JPanel();

        firstThetaRadioButton = new JRadioButton();
        firstThetaRadioButton.setText("Negative theta");
        firstThetaRadioButton.setPreferredSize(new Dimension(120, 25));
        firstThetaRadioButton.addActionListener(event -> {
            BigDecimal theta = numbersInfo.getTheta();
            if (theta != null) {
                if (theta.compareTo(BigDecimal.ZERO) > 0) {
                    theta = theta.negate();
                }
                numbersInfo.setSelectedTheta(theta);
            }
        });

        secondThetaRadioButton = new JRadioButton();
        secondThetaRadioButton.setText("Positive theta");
        secondThetaRadioButton.setPreferredSize(new Dimension(120, 25));
        secondThetaRadioButton.addActionListener(event -> {
            BigDecimal theta = numbersInfo.getTheta();
            if (theta != null) {
                if (theta.compareTo(BigDecimal.ZERO) < 0) {
                    theta = theta.negate();
                }
                numbersInfo.setSelectedTheta(theta);
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(firstThetaRadioButton);
        buttonGroup.add(secondThetaRadioButton);

        thetaCheckBoxPanel.add(firstThetaRadioButton);
        thetaCheckBoxPanel.add(secondThetaRadioButton);

        thetaCheckBoxPanel.setAlignmentX(CENTER_ALIGNMENT);
    }

    private JButton initAnalyzeInputButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 25));
        button.setText("Analyze");
        button.setAlignmentX(0.5f);
        button.addActionListener(event -> {
            if (resultLabel != null) {
                resultLabel.setVisible(false);
            }
            List<Integer> bipolarValues = inputSymbolLabels.stream()
                    .map(label -> "X".equalsIgnoreCase(label.getText()) ? 1 : -1)
                    .collect(Collectors.toList());

            if (bipolarValues.equals(numbersInfo.getLastSymbolBipolarValues())) {
                List<Integer> lastOutputNeurons = new ArrayList<>(numbersInfo.getLastSymbolOutputValues());
                List<BigDecimal> calculatedInput = new ArrayList<>(
                        hopfieldAnalyzer.calculateNeuronInputs(lastOutputNeurons));
                List<Integer> calculatedOutput = new ArrayList<>(
                        hopfieldAnalyzer.calculateNeuronOutputs(lastOutputNeurons, calculatedInput));

                resultPanel.add(Box.createRigidArea(new Dimension(20, 0)));

                updateResultTable(lastOutputNeurons, calculatedInput, calculatedOutput);

                numbersInfo.setLastSymbolBipolarValues(bipolarValues);
                numbersInfo.setLastSymbolOutputValues(calculatedOutput);
            } else {
                List<BigDecimal> calculatedInput = new ArrayList<>(
                        hopfieldAnalyzer.calculateNeuronInputs(bipolarValues));
                List<Integer> calculatedOutput = new ArrayList<>(
                        hopfieldAnalyzer.calculateNeuronOutputs(bipolarValues, calculatedInput));

                initResultPanel();
                updateResultTable(bipolarValues, calculatedInput, calculatedOutput);

                numbersInfo.setLastSymbolBipolarValues(bipolarValues);
                numbersInfo.setLastSymbolOutputValues(calculatedOutput);

                submainPanel.add(resultPanel);
            }

            List<List<Integer>> idealBipolarValues = numbersInfo.getIdealSymbolsBipolarValues();
            List<Integer> lastBipolarOutput = numbersInfo.getLastSymbolOutputValues();
            int index = idealBipolarValues.indexOf(lastBipolarOutput);
            if (index != -1) {
                String symbol = numbersInfo.getIdealSymbolNames().get(index);
                resultLabel.setText(String.format("Result is %s", symbol));
                resultLabel.setVisible(true);
            }
            updateUI();
        });
        return button;
    }

    public void updateThetaRadioButtons() {
        BigDecimal thetaValue = numbersInfo.getTheta().abs();
        firstThetaRadioButton.setText(String.format("Theta = %s", thetaValue.negate()));
        secondThetaRadioButton.setText(String.format("Theta = %s", thetaValue));
    }

    private void initResultPanel() {
        ofNullable(resultPanel).ifPresent(panel -> submainPanel.remove(panel));
        resultPanel = new JPanel();
        BoxLayout panelLayout = new BoxLayout(resultPanel, BoxLayout.X_AXIS);
        resultPanel.setLayout(panelLayout);
    }

    private void updateResultTable(List<Integer> bipolarValues, List<BigDecimal> inputNeurons,
            List<Integer> outputNeurons) {
        JPanel resultTablePanel = new JPanel();
        resultTablePanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(60, 20);

        //numbers column
        JLabel label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        resultTablePanel.add(label);

        for (int i = 1; i <= bipolarValues.size(); i++) {
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(String.valueOf(i));

            gridBagConstraints.gridy = i;

            resultTablePanel.add(label, gridBagConstraints);
        }

        int currentGridY = 0;
        int currentGridX = 1;

        //bipolar component values
        gridBagConstraints.gridy = currentGridY++;
        gridBagConstraints.gridx = currentGridX++;

        label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        label.setText("Bipolars");
        resultTablePanel.add(label, gridBagConstraints);

        for (Integer bipolarValue : bipolarValues) {
            gridBagConstraints.gridy = currentGridY++;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(String.valueOf(bipolarValue));
            resultTablePanel.add(label, gridBagConstraints);
        }
        currentGridX++;

        //neuron input values
        currentGridY = 0;
        gridBagConstraints.gridy = currentGridY++;
        gridBagConstraints.gridx = currentGridX;

        label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        label.setText("Input");
        resultTablePanel.add(label, gridBagConstraints);

        for (BigDecimal inputValue : inputNeurons) {
            gridBagConstraints.gridy = currentGridY++;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(String.valueOf(inputValue));
            resultTablePanel.add(label, gridBagConstraints);
        }
        currentGridX++;

        //neuron output values
        currentGridY = 0;
        gridBagConstraints.gridy = currentGridY++;
        gridBagConstraints.gridx = currentGridX;

        label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        label.setText("Output");
        resultTablePanel.add(label, gridBagConstraints);

        for (Integer outputValue : outputNeurons) {
            gridBagConstraints.gridy = currentGridY++;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(String.valueOf(outputValue));
            resultTablePanel.add(label, gridBagConstraints);
        }
        resultPanel.add(resultTablePanel);
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
