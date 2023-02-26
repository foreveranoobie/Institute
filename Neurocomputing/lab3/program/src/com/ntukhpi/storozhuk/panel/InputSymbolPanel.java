package com.ntukhpi.storozhuk.panel;

import static com.ntukhpi.storozhuk.util.StringUtil.A_SYMBOL_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.T_SYMBOL_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.U_SYMBOL_SIGN;
import static com.ntukhpi.storozhuk.util.StringUtil.Z_NEURON_SIGN;
import static java.util.Optional.ofNullable;
import static javax.swing.SwingConstants.CENTER;

import com.ntukhpi.storozhuk.analyzer.HammingAnalyzer;
import com.ntukhpi.storozhuk.analyzer.MaxnetAnalyzer;
import com.ntukhpi.storozhuk.util.NumbersInfo;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class InputSymbolPanel extends JPanel {

    private final NumbersInfo numbersInfo;
    private final HammingAnalyzer hammingAnalyzer;
    private final MaxnetAnalyzer maxnetAnalyzer;

    private JPanel submainPanel;
    private List<JLabel> inputLabels;
    private List<JLabel> zInputLabels;
    private List<JLabel> zOutputLabels;
    private JPanel iterativeOutputsPanel;
    private JTextField kField;
    private JTextField epsilonField;

    public InputSymbolPanel(NumbersInfo numbersInfo, HammingAnalyzer hammingAnalyzer, MaxnetAnalyzer maxnetAnalyzer) {
        super();
        this.numbersInfo = numbersInfo;
        this.hammingAnalyzer = hammingAnalyzer;
        this.maxnetAnalyzer = maxnetAnalyzer;

        inputLabels = new ArrayList<>(numbersInfo.getSymbolsColumnsCount() * numbersInfo.getSymbolsRowsCount());
        zInputLabels = new ArrayList<>(numbersInfo.getzNeuronsCount());
        zOutputLabels = new ArrayList<>(numbersInfo.getzNeuronsCount());

        submainPanel = new JPanel();
        BoxLayout panelLayout = new BoxLayout(submainPanel, BoxLayout.Y_AXIS);
        submainPanel.setLayout(panelLayout);

        submainPanel.add(initInputSymbolPanel());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initAnalyzeInputButton());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addKAndEpsilonFields();
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initTableOfZInputs());
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        submainPanel.add(initTableOfZOutputs());

        add(submainPanel);
    }

    private JPanel initInputSymbolPanel() {
        JPanel symbolPanel = new JPanel();
        symbolPanel.setLayout(new GridBagLayout());

        GridBagConstraints horizontalConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(25, 25);

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
                inputLabels.add(label);
            }
        }

        return symbolPanel;
    }

    private JPanel initTableOfZInputs() {
        JPanel zInputsPanel = new JPanel();
        zInputsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(45, 25);

        JLabel label;

        for (int i = 0; i < numbersInfo.getzNeuronsCount(); i++) {
            gridBagConstraints.gridx = i;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setHorizontalAlignment(0);
            label.setText(String.format("%sin(%s%d)", U_SYMBOL_SIGN, Z_NEURON_SIGN, i + 1));
            zInputsPanel.add(label, gridBagConstraints);
        }

        gridBagConstraints.gridy = 1;
        for (int i = 0; i < numbersInfo.getzNeuronsCount(); i++) {
            gridBagConstraints.gridx = i;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setHorizontalAlignment(0);
            zInputLabels.add(label);
            zInputsPanel.add(label, gridBagConstraints);
        }

        return zInputsPanel;
    }

    private JPanel initTableOfZOutputs() {
        JPanel zOutputsPanel = new JPanel();
        zOutputsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(55, 25);

        JLabel label;

        for (int i = 0; i < numbersInfo.getzNeuronsCount(); i++) {
            gridBagConstraints.gridx = i;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setHorizontalAlignment(0);
            label.setText(String.format("%sout(%s%d)", U_SYMBOL_SIGN, Z_NEURON_SIGN, i + 1));
            zOutputsPanel.add(label, gridBagConstraints);
        }

        gridBagConstraints.gridy = 1;
        for (int i = 0; i < numbersInfo.getzNeuronsCount(); i++) {
            gridBagConstraints.gridx = i;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setHorizontalAlignment(0);
            zOutputLabels.add(label);
            zOutputsPanel.add(label, gridBagConstraints);
        }

        return zOutputsPanel;
    }

    private JButton initAnalyzeInputButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(50, 25));
        button.setText("Analyze");
        button.setAlignmentX(0.5f);
        button.addActionListener(event -> {
            numbersInfo.setK(BigDecimal.valueOf(Float.parseFloat(kField.getText())));
            numbersInfo.setEpsilon(BigDecimal.valueOf(Float.parseFloat(epsilonField.getText())));
            List<Integer> bipolarSymbolsInput = inputLabels.stream()
                    .map(label -> "X".equals(label.getText()) ? 1 : -1)
                    .collect(Collectors.toList());
            hammingAnalyzer.calculateUInputsForInputSymbol(bipolarSymbolsInput);
            updateZNeuronsInputs();

            hammingAnalyzer.calculateUOutputsForZNeurons();
            updateZNeuronsOutputs();

            maxnetAnalyzer.calculateOutput();

            ofNullable(iterativeOutputsPanel)
                    .ifPresentOrElse(panel -> submainPanel.remove(panel),
                            () -> submainPanel.add(Box.createRigidArea(new Dimension(0, 20))));

            initIterativeOutputsTable();
        });
        return button;
    }

    private void initIterativeOutputsTable() {
        iterativeOutputsPanel = new JPanel();
        iterativeOutputsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(55, 25);

        JLabel label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        iterativeOutputsPanel.add(label, gridBagConstraints);

        for (int i = 0; i < numbersInfo.getTotalSymbolsCount(); i++) {
            gridBagConstraints.gridx = i + 1;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(String.format("%sout(%s%d)", U_SYMBOL_SIGN, A_SYMBOL_SIGN, i + 1));
            iterativeOutputsPanel.add(label, gridBagConstraints);
        }

        for (int i = 0; i < numbersInfo.getResultValuesIterations().size(); i++) {
            gridBagConstraints.gridx = 0;
            initOutputIterativeRow(i, numbersInfo.getResultValuesIterations().get(i), gridBagConstraints);
        }

        submainPanel.add(iterativeOutputsPanel);
        updateUI();
    }

    private void initOutputIterativeRow(int rowNum, List<BigDecimal> values, GridBagConstraints gridBagConstraints) {
        Border layoutBorder = new MatteBorder(1, 1, 1, 1, Color.BLACK);
        Dimension preferredLabelSize = new Dimension(55, 25);

        gridBagConstraints.gridy = rowNum + 1;

        JLabel label = new JLabel();
        label.setBorder(layoutBorder);
        label.setPreferredSize(preferredLabelSize);
        label.setText(String.format("%s%d", T_SYMBOL_SIGN, rowNum));
        iterativeOutputsPanel.add(label, gridBagConstraints);

        int numX = 1;

        for (BigDecimal value : values) {
            gridBagConstraints.gridx = numX++;
            label = new JLabel();
            label.setBorder(layoutBorder);
            label.setBorder(layoutBorder);
            label.setPreferredSize(preferredLabelSize);
            label.setText(value.toString());

            iterativeOutputsPanel.add(label, gridBagConstraints);
        }
    }

    private void updateZNeuronsInputs() {
        List<BigDecimal> zInputValues = numbersInfo.getuInputValuesForZNeurons();
        int index = 0;
        for (JLabel label : zInputLabels) {
            label.setText(zInputValues.get(index++).toString());
        }
    }

    private void updateZNeuronsOutputs() {
        List<BigDecimal> zOutputValues = numbersInfo.getuOutputValuesForZNeurons();
        int index = 0;
        for (JLabel label : zOutputLabels) {
            label.setText(zOutputValues.get(index++).toString());
        }
    }

    private void addKAndEpsilonFields() {
        Dimension preferredLabelSize = new Dimension(20, 20);

        JPanel fieldPanel = new JPanel();

        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setPreferredSize(preferredLabelSize);
        descriptionLabel.setText("k");

        kField = new JTextField();
        kField.setPreferredSize(new Dimension(100, 20));

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(kField);
        submainPanel.add(fieldPanel);
        submainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        fieldPanel = new JPanel();
        descriptionLabel = new JLabel();
        descriptionLabel.setPreferredSize(new Dimension(45, 20));
        descriptionLabel.setText("epsilon");

        epsilonField = new JTextField();
        epsilonField.setPreferredSize(new Dimension(100, 20));
        fieldPanel.add(descriptionLabel);
        fieldPanel.add(epsilonField);

        submainPanel.add(fieldPanel);
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
